/* Grader.java
 * Calculates grades based on weight provided by subjects using TaskType
 */

package quickplanner.workers;

import java.util.ArrayList;
import java.util.Arrays;

// score percent = scored points / total points * 100
// avg: (score% * weight% + score% * weight% + ...) / weights

@SuppressWarnings("unused")
public class Grader {
	public static double calculateGrade(Planner planner, String subject, TaskType[] types, double[] weights) {
		ArrayList<Task> filteredTasks = new ArrayList<>();
		planner.getTasks().forEach(task -> { if (subject.equals(task.getSubject())) filteredTasks.add(task); });

		double[] scores = new double[filteredTasks.size()];
		for (int i = 0; i < scores.length; ++i) {
			scores[i] = (filteredTasks.get(i).getScoredPoints() / filteredTasks.get(i).getTotalPoints()) * 100.0d;
			scores[i] *= weights[Arrays.asList(types).indexOf(filteredTasks.get(i).getType())];
		}

		return Arrays.stream(scores).sum() / Arrays.stream(weights).sum();
	}
}