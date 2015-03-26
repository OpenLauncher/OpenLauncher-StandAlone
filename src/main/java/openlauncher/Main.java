package openlauncher;


import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main extends JFrame {

	static File mcDir;
	static File mcExe;
	static File mcLauncher;
	static File workDir;
	static File forgeDir;
	static String instaceName = "test";
	static String forgeVersion = "10.13.2.1342";
	static String minecraftVersion = "1.7.10";

	public JTextArea textArea;
	public JScrollPane scrollPane;
	private final Font MONOSPACED = new Font("Monospaced", 0, 12);
	private StringBuilder outputBuffer = new StringBuilder();


	public Main() throws HeadlessException {
		super("OpenLauncher");
		setSize(854, 480);
		setDefaultCloseOperation(3);

		this.textArea = new JTextArea();
		this.textArea.setLineWrap(true);
		this.textArea.setEditable(false);
		this.textArea.setFont(MONOSPACED);
		((DefaultCaret) this.textArea.getCaret()).setUpdatePolicy(1);

		this.scrollPane = new JScrollPane(this.textArea);
		this.scrollPane.setBorder(null);
		this.scrollPane.setVerticalScrollBarPolicy(22);

		add(this.scrollPane);
		setLocationRelativeTo(null);
		setVisible(true);

		println("Starting the openLauncher");
	}

	public void start() {
		println(getHome().getAbsolutePath());

		if (!getHome().exists()) {
			println("Creating openLauncher folder at:" + getHome().getAbsolutePath());
			getHome().mkdirs();
		}

		mcDir = new File(getHome().getAbsoluteFile() + "/", "Minecraft");
		if (!mcDir.exists())
			mcDir.mkdirs();

		workDir = new File(getHome().getAbsoluteFile() + "/", instaceName);
		if (!workDir.exists())
			workDir.mkdirs();

		forgeDir = new File(getHome().getAbsoluteFile() + "/", "forge");
		if (!forgeDir.exists())
			forgeDir.mkdirs();

		//We will use a jar now
		mcExe = new File(mcDir, "MinecraftLauncher.jar");
		if (!mcExe.exists()) {
			//We need to download the minecraft jar file
			println("Downloading the minecraft launcher bootstrap...");
			try {
				DownloadUtils.downloadFile("https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar", mcDir, "MinecraftLauncher.jar");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		addToClasspath(mcExe);

		mcBootstrap.downloadLauncher(this);

		mcLauncher = new File(mcDir, "launcher.jar");

		addToClasspath(mcLauncher);

		println("Creating the custom profiles");

		try {
			if (forgeVersion != "") {
				profileCreator.createProfile(mcDir, minecraftVersion + "-Forge" + forgeVersion + "-" + minecraftVersion, instaceName, workDir);
			} else {
				profileCreator.createProfile(mcDir, minecraftVersion, instaceName, workDir);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		if (forgeVersion != "") {
			File mcverDir = new File(mcDir, "versions/" + minecraftVersion);
			if (!mcverDir.exists()) {
				println("Downloading minecraft");
				MinecraftVersionInstaller.installMc(minecraftVersion, this);
			}
			println("Using forge");
			File forgeInstaller = new File(forgeDir, "forge-" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "-installer.jar");
			File forgeInstallLocation = new File(mcDir, "versions/" + minecraftVersion + "-Forge" + forgeVersion + "-" + minecraftVersion);
			if (forgeInstaller.exists() && !(forgeInstallLocation.exists())) {
				addToClasspath(forgeInstaller);
				println("Installing forge");
				ForgeInstaller.installForge(mcDir);
			}
		}

		println("Starting the minecraft launcher");

		try {
			Process proc = Runtime.getRuntime().exec("java -jar " + mcExe.getAbsolutePath() + " -workDir " + mcDir.getAbsolutePath());
			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			println("<MINECRAFT>");

			while ((line = br.readLine()) != null)
				println(line);

			println("</MINECRAFT>");
			int exitVal = proc.waitFor();
			println("Process exitValue: " + exitVal);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static File getHome() {
		if (isLinux()) {
			try {
				return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()
						.getSchemeSpecificPart()).getParentFile();
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return new File(System.getProperty("user.dir"), "OpenLauncher");
			}
		} else {
			return new File(System.getProperty("user.dir"), "OpenLauncher");
		}
	}

	public static boolean isWindows() {
		return OperatingSystem.getOS() == OperatingSystem.WINDOWS;
	}

	public static boolean isMac() {
		return OperatingSystem.getOS() == OperatingSystem.OSX;
	}


	public static boolean isLinux() {
		return OperatingSystem.getOS() == OperatingSystem.LINUX;
	}

	public static boolean is64Bit() {
		return System.getProperty("sun.arch.data.model").contains("64");
	}

	/**
	 * Credit to https://github.com/Slowpoke101/FTBLaunch/blob/master/src/main/java/net/ftb/workers/AuthlibDLWorker.java
	 */
	public boolean addToClasspath(File file) {
		println("Loading external library " + file.getName() + " to classpath");
		try {
			if (file.exists()) {
				addURL(file.toURI().toURL());
			} else {
				println("Error loading jar");
			}
		} catch (Throwable t) {
			if (t.getMessage() != null) {
				println(t.getMessage());
			}
			return false;
		}

		return true;
	}

	/**
	 * Credit to https://github.com/Slowpoke101/FTBLaunch/blob/master/src/main/java/net/ftb/workers/AuthlibDLWorker.java
	 */
	public void addURL(URL u) throws IOException {
		URLClassLoader sysloader = (URLClassLoader) this.getClass().getClassLoader();
		Class sysclass = URLClassLoader.class;
		try {
			Method method = sysclass.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(sysloader, u);
		} catch (Throwable t) {
			if (t.getMessage() != null) {
				println(t.getMessage());
			}
			throw new IOException("Error, could not add URL to system classloader");
		}
	}

	public void println(String string) {
		print(new StringBuilder().append(string).append("\n").toString());
	}

	public void print(String string) {
		System.out.print(string);

		outputBuffer.append(string);

		Document document = textArea.getDocument();
		final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();

		boolean shouldScroll = scrollBar.getValue() + scrollBar.getSize().getHeight() + MONOSPACED.getSize() * 2 > scrollBar.getMaximum();
		try {
			document.insertString(document.getLength(), string, null);
		} catch (BadLocationException ignored) {
		}
		if (shouldScroll)
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollBar.setValue(2147483647);
				}
			});
	}
}
