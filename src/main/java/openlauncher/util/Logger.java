package openlauncher.util;

public class Logger {

	private String channel;

	private boolean verbose;

	public Logger(String channel) {
		this(channel, false);
	}

	public Logger(String channel, boolean enableVerbose) {
		this.channel = channel;
		this.verbose = enableVerbose;
	}

	public Logger log(LogLevel level, String msg) {
		String chan = "[" + this.channel + "]";
		switch (level) {
			case DEBUG:
				if (verbose) System.out.println(chan + "[DEBUG]" + msg);
				break;
			case INFO:
				System.out.println(chan + "[INFO]" + msg);
				break;
			case WARN:
				System.err.println(chan + "[WARN]" + msg);
				break;
			case ERROR:
				System.err.println(chan + "[ERROR]" + msg);
				break;
		}
		return this;
	}

	public Logger debug(String msg) {
		return log(LogLevel.DEBUG, msg);
	}

	public Logger debug(String msg, Object... params) {
		return debug(String.format(msg, params));
	}

	public Logger info(String msg) {
		return log(LogLevel.INFO, msg);
	}

	public Logger info(String msg, Object... params) {
		return info(String.format(msg, params));
	}

	public Logger warn(String msg) {
		return log(LogLevel.WARN, msg);
	}

	public Logger warn(String msg, Object... params) {
		return warn(String.format(msg, params));
	}

	public Logger error(String msg) {
		return log(LogLevel.ERROR, msg);
	}

	public Logger error(String msg, Object... params) {
		return error(String.format(msg, params));
	}

	public void setVerboseity(boolean enableVerbose) {
		if (verbose != enableVerbose) {
			warn("Verbosity set to " + enableVerbose);
		}
		verbose = enableVerbose;
	}

	/**
	 * Logs a stack trace
	 *
	 * @param exception The exception
	 */
	public void logStackTrace(Exception exception) {
		exception.printStackTrace();
		error(exception.getMessage());
		for (StackTraceElement element : exception.getStackTrace()) {
			if (element.toString() != null) {
				error(element.toString());
			}
		}
	}
}
