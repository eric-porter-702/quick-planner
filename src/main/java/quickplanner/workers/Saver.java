// Supposedly saves CSV to a local directory

package quickplanner.workers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

@SuppressWarnings("unused")
// Creates an auto save feature
// Usage: Saver <name> = new Saver(file name, save period in seconds, the planner to save)
// Run setAutoSave() to activate with a set period
// Run stopAutoSave() to stop or change the period
// IMPORTANT: USE shutdown() to kill the thread after stopping autoSave when closing the program
public class Saver {

	private long period;
	private Planner planner;
	private String fileName, filePath;
	private ScheduledFuture<?> autoSaverHandle;
	private final ScheduledExecutorService saver = Executors.newScheduledThreadPool(1);
	public static File lastSaved;

	// getters
	public long getPeriod() { return period; }
	public Planner getPlanner() { return planner; }
	public String getFileName() { return fileName; }
	public String getFilePath() { return filePath; }

	// setters
	public void setPeriod(long period) { this.period = period; }
	public void setPlanner(Planner planner) { this.planner = planner; }

	public void shutdown() { saver.shutdownNow(); }

	public void setFile(String fileName) {
		this.fileName = fileName;
		filePath = System.getProperty("user.home")
				+ System.getProperty("file.separator")
				+ "QuickPlanner"
				+ System.getProperty("file.separator")
				+ fileName;
	}

	// save methods
	public void save() {
		CSVUtil.exportCSV(planner, filePath);
		System.out.println("Saved to " + fileName + "!");
		try {
			lastSaved = new File(
					System.getProperty("user.home")
							+ System.getProperty("file.separator")
							+ "QuickPlanner"
							+ System.getProperty("file.separator")
							+ "last_saved"
			);
			if (lastSaved.getParentFile().mkdirs())
				System.out.println("created new directory");
			if (lastSaved.createNewFile())
				System.out.println("File created: " + lastSaved.getName());
			else
				System.out.println("File exists already.");
			BufferedWriter bW = new BufferedWriter(new FileWriter(lastSaved.getAbsoluteFile()));
			bW.write(fileName);
			bW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// auto save implementation
	public void setAutoSave() {
		autoSaverHandle = saver.scheduleAtFixedRate(
				this::save,
				0,
				period,
				SECONDS
		);
	}

	public void stopAutoSave() { saver.execute(() -> autoSaverHandle.cancel(true)); }

	public Saver(String fileName, long period, Planner planner) {
		this.period = period;
		this.fileName = fileName;
		this.planner = planner;
		setFile(fileName);
	}

	public Saver(String fileName, Planner planner){
		this.fileName = fileName;
		this.planner = planner;
		setFile(fileName);
	}
}
