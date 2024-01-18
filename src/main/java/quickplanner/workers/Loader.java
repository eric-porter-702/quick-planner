package quickplanner.workers;

public class Loader {
	private String filePath;

	// setter
	public void setFile(String fileName) {
		filePath = System.getProperty("user.home")
				+ System.getProperty("file.separator")
				+ "QuickPlanner"
				+ System.getProperty("file.separator")
				+ fileName;
	}

	public Planner load() {
		return CSVUtil.importCSV(filePath);
	}

	public Loader(String fileName) {
		setFile(fileName);
	}
}