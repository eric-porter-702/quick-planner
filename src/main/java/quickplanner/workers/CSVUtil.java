/* CSVUtil.java
 * Exports Planner as a CSV
 */

package quickplanner.workers;

import java.io.*;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class CSVUtil {
	public static void exportCSV(Planner planner, String fileName) {
		try {
			File csv = new File(fileName + ".csv");
			if (csv.getParentFile().mkdirs())
				System.out.println("Directory Created: " + csv.getPath());
			if (csv.createNewFile())
				System.out.println("File created: " + csv.getName());
			else
				System.out.println("File exists already.");

			FileWriter writer = new FileWriter(csv.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			planner.getTasks().forEach(task -> {
				String name = task.getName();
				String subject = task.getSubject();
				String description = task.getDescription();
				LocalDateTime dueDate = task.getDueDate();
				float totalPoints = task.getTotalPoints();
				float scoredPoints = task.getScoredPoints();
				boolean extraCredit = task.isExtraCredit();
				boolean status = task.getStatus();
				TaskType type = task.getType();
				int id = task.getId();
				try {
					bufferedWriter.write("%s,%s,%s,%s,%s,%s,%s,%s,%s,%d\n".formatted(
							name,
							subject,
							description,
							dueDate,
							totalPoints,
							scoredPoints,
							extraCredit,
							status,
							type,
							id
					));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
			bufferedWriter.close();
		} catch (IOException e) {
			System.err.print("Error: ");
			e.printStackTrace();
		}
	}

	public static Planner importCSV(String fileName) {
		Planner pl = new Planner();
		try {
			File csv = new File(fileName + ".csv");
			if (csv.createNewFile())
				System.err.println("CSV DOES NOT EXIST!");
			else {
				try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
					Stream<String> lines = bufferedReader.lines();

					lines.forEach(line -> {
						String[] task = line.split(",");
						pl.addTask(new Task(
								task[0],
								task[1],
								LocalDateTime.parse(task[3]),
								Float.parseFloat(task[4]),
								Float.parseFloat(task[5]),
								Boolean.parseBoolean(task[6]),
								Boolean.parseBoolean(task[7]),
								TaskType.valueOf(task[8]),
								task[2]
						));
					});
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pl;
	}
}