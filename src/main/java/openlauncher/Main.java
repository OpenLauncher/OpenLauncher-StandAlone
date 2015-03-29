package openlauncher;


import openlauncher.gui.LauncherForm;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main{

	static File mcDir;
	static File mcExe;
	static File mcLauncher;
	static File workDir;
	static File forgeDir;
	static File libsDir;
	String instanceName;
	String forgeVersion;
	String minecraftVersion;

	private final Font MONOSPACED = new Font("Monospaced", 0, 12);
	private StringBuilder outputBuffer = new StringBuilder();

	public void start(LauncherForm form){
		Launch.form = form;
		println("Starting the openLauncher");

		libsDir = new File(getHome().getAbsoluteFile() + "/", "libs");
		if(!libsDir.exists())
			libsDir.mkdirs();

		File commons = new File(libsDir, "commons-io-2.4.jar");
		if(!commons.exists()){
			print("Downloading Commons-io");
			try{
				URL website = new URL("http://central.maven.org/maven2/commons-io/commons-io/2.4/commons-io-2.4.jar");
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(commons);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (MalformedURLException e){
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addToClasspath(commons);

		File javaJson = new File(libsDir, "javax.json-1.0.4.jar");
		if(!javaJson.exists()){
			print("Downloading javax.json-1.0.4.jar");
			try{
				URL website = new URL("http://search.maven.org/remotecontent?filepath=org/glassfish/javax.json/1.0.4/javax.json-1.0.4.jar");
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(javaJson);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (MalformedURLException e){
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addToClasspath(javaJson);

		//TODO add a way to load the mod packs from the old launcher
		File packsJson = new File(getHome(), "packs.json");
		try {
			println("Downloading the pack list");
			DownloadUtils.downloadFile("http://modmuss50.me/files/packs.json", getHome(), "packs.json");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(packsJson.exists()){
			try {
				println("Reading the pack list");
				new PackLoader(this).loadPacks(form);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void launch(String instaceName, String forgeVersion, String minecraftVersion) {
		this.instanceName = instaceName;
		this.forgeVersion = forgeVersion;
		this.minecraftVersion = minecraftVersion;

		println("Starting " + instaceName);
		Launch.form.progressBar1.setValue(1);


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

		Launch.form.progressBar1.setValue(2);

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

		Launch.form.progressBar1.setValue(3);
		addToClasspath(mcExe);

		mcBootstrap.downloadLauncher(this);

		Launch.form.progressBar1.setValue(4);

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

		Launch.form.progressBar1.setValue(5);

		if (forgeVersion != "") {
			File mcverDir = new File(mcDir, "versions/" + minecraftVersion);
			if (!mcverDir.exists()) {
				println("Downloading minecraft");
				MinecraftVersionInstaller.installMc(minecraftVersion, this);
			}
			Launch.form.progressBar1.setValue(6);
			println("Using forge");
			File forgeInstaller = new File(forgeDir, "forge-" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "-installer.jar");
			if(!forgeInstaller.exists()){
				try {
					DownloadUtils.downloadFile("http://files.minecraftforge.net/maven/net/minecraftforge/forge/" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "/forge-" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "-installer.jar", forgeDir, forgeInstaller.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			addToClasspath(forgeInstaller);
			File forgeInstallLocation = new File(mcDir, "versions/" + minecraftVersion + "-Forge" + forgeVersion + "-" + minecraftVersion);
			if (forgeInstaller.exists() && !(forgeInstallLocation.exists())) {
				println("Installing forge");
				ForgeInstaller.installForge(mcDir);
			}
		}

		println("Starting the minecraft launcher");
		Launch.form.progressBar1.setValue(7);

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

		//Launch.form.textLog.append(string);
		if(Launch.form == null)
			return;

		Document document = Launch.form.textLog.getDocument();
		final JScrollBar scrollBar = Launch.form.scrollBar1;

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
