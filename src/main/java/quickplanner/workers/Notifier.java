package quickplanner.workers;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

public class Notifier {
	private final ScheduledExecutorService notificationService = Executors.newScheduledThreadPool(0);
	private final ScheduledExecutorService reNotifierService = Executors.newScheduledThreadPool(0);
	private final TrayIcon trayIcon;
	private final Map<Boolean, ArrayList<Task>> notified = new HashMap<>();
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a d MMMM yyyy");
	private Planner planner;
	private long period;
	private ScheduledFuture<?> notifierHandle, reNotifierHandle;

	private Notifier(Planner planner) throws IOException {
		final SystemTray tray;
		this.planner = planner;
		this.period = 60L;

		if (SystemTray.isSupported()) {
			tray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(
					ImageIO.read(Objects.requireNonNull(
							getClass().getResource("/quickplanner/application/logo_img/LogoToHome.png"))),
					"Quick Planner");
			try {
				trayIcon.setImageAutoSize(true);
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("AWTException caught");
			}
		} else {
			trayIcon = null;
		}

		notified.put(false, new ArrayList<>(0));

		this.planner.getTasks().forEach(task -> {
			ArrayList<Task> tasks = notified.get(false);
			tasks.add(task);
			notified.put(false, tasks);
		});

		notified.get(false).sort(Comparator.comparing(Task::getDueDate));
	}

	public Notifier(Planner planner, long period) throws IOException {
		this(planner);
		this.period = period;
	}

	public void setPlanner(Planner planner) {
		this.planner = planner;
		notified.put(false, new ArrayList<>(0));

		this.planner.getTasks().forEach(task -> {
			ArrayList<Task> tasks = notified.get(false);
			tasks.add(task);
			notified.put(false, tasks);
		});

		notified.get(false).sort(Comparator.comparing(Task::getDueDate));
	}

	public void sendNotification(String title, String message) {
		String os = System.getProperty("os.name");
		try {
			if (os.contains("Linux")) {
				ProcessBuilder processBuilder = new ProcessBuilder(
						"zenity",
						"--notification",
						"--title=" + title,
						"--text=" + message);
				processBuilder.inheritIO().start();
			} else if (os.contains("Mac")) {
				ProcessBuilder processBuilder = new ProcessBuilder(
						"osascript",
						"-e",
						"display notification \"" + message + "\"" + " with title \"" + title + "\""
				);
				processBuilder.inheritIO().start();
			} else if (SystemTray.isSupported()) {
				trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
			}
		} catch (IOException e) {
			System.err.println("Could not send not notification, IOException");
		}
	}

	public void pushNotification() {
		ArrayList<Task> notNotified = new ArrayList<>(notified.get(false));
		AtomicInteger tasksDue = new AtomicInteger();
		String title, message;

		notNotified.forEach(t -> {
			if ((Duration.between(LocalDateTime.now(), t.getDueDate()).toMinutes() <= period)
				&& (Duration.between(LocalDateTime.now(), t.getDueDate()).toMinutes() > 0)
					&& (!t.getStatus())
			) {
				tasksDue.getAndIncrement();
				System.out.println(Duration.between(LocalDateTime.now(), t.getDueDate()).toMinutes());
				notified.computeIfAbsent(true, k -> new ArrayList<>());
				notified.get(true).add(t);
				notified.get(false).remove(t);
			}
		});

		Task task = notNotified.stream().filter(t -> !t.getStatus()).findFirst().orElse(null);
		assert task != null;
		if (tasksDue.intValue() == 1) {
			title = "Task Due Soon";
			message = "%s is due at %s".formatted(
					task.getName(),
					task.getDueDate().format(formatter)
			);
			sendNotification(title, message);
		} else if ((tasksDue.intValue() > 1)) {
			title = "Tasks Due Soon";
			message = "%s is due at %s, and %d other task(s) are due soon".formatted(
					task.getName(),
					task.getDueDate().format(formatter),
					tasksDue.intValue() - 1
			);
			sendNotification(title, message);
		}
	}

	public void resetNotifications() {
		notified.clear();
		setPlanner(planner);
	}

	public void setNotificationService() {
		notifierHandle = notificationService.scheduleAtFixedRate(
				this::pushNotification,
				0L,
				15L,
				TimeUnit.SECONDS
		);
	}

	public void setReNotifierService() {
		reNotifierHandle = reNotifierService.scheduleAtFixedRate(
				this::resetNotifications,
				0,
				period,
				TimeUnit.MINUTES
		);
	}

	public void pause() {
		notificationService.execute(() -> notifierHandle.cancel(true));
		reNotifierService.execute(() -> reNotifierHandle.cancel(true));
	}

	public void shutdown() {
		notificationService.shutdownNow();
		reNotifierService.shutdownNow();
	}
}
