package openlauncher;

public enum OperatingSystem {
	LINUX("linux"), WINDOWS("windows"), OSX("osx");

	private final String name;

	private OperatingSystem(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static OperatingSystem getOS() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return OperatingSystem.WINDOWS;
		} else if (osName.contains("mac")) {
			return OperatingSystem.OSX;
		} else {
			return OperatingSystem.LINUX;
		}
	}

	public static String getVersion() {
		return System.getProperty("os.version");
	}
}
