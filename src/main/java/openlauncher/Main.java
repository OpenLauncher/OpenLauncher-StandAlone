package openlauncher;


import gui.OpenLauncherGui;

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

public class Main {

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

	public void start(OpenLauncherGui form) {
		Launch.form = form;
		println("Starting the openLauncher");

		libsDir = new File(getHome().getAbsoluteFile() + "/", "libs");
		if (!libsDir.exists())
			libsDir.mkdirs();

		File commons = new File(libsDir, "commons-io-2.4.jar");
		if (!commons.exists()) {
			println("Downloading commons-io-2.4.jar");
			try {
				URL website = new URL("http://central.maven.org/maven2/commons-io/commons-io/2.4/commons-io-2.4.jar");
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(commons);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addToClasspath(commons);

		File javaJson = new File(libsDir, "javax.json-1.0.4.jar");
		if (!javaJson.exists()) {
			println("Downloading javax.json-1.0.4.jar");
			try {
				URL website = new URL("https://repo1.maven.org/maven2/org/glassfish/javax.json/1.0.4/javax.json-1.0.4.jar");
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(javaJson);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addToClasspath(javaJson);

		File gsonJar = new File(libsDir, "gson-2.3.1.jar");
		if (!gsonJar.exists()) {
			println("Downloading gson-2.3.1.jar");
			try {
				URL website = new URL("http://central.maven.org/maven2/com/google/code/gson/gson/2.3.1/gson-2.3.1.jar");
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(gsonJar);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addToClasspath(gsonJar);

		File packsJson = new File(getHome(), "packs.json");
		try {
			println("Downloading the pack list");
			DownloadUtils.downloadFile("http://www.creeperrepo.net/OpenLauncher/launcher/packs.json", getHome(), "packs.json");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (packsJson.exists()) {
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
	//	Launch.form.progressBar1.setValue(1);


		println(getHome().getAbsolutePath());

		if (!getHome().exists()) {
			println("Creating openLauncher folder at:" + getHome().getAbsolutePath());
			getHome().mkdirs();
		}

		mcDir = new File(getHome().getAbsoluteFile() + "/", "Minecraft");
		if (!mcDir.exists())
			mcDir.mkdirs();

		workDir = new File(getHome().getAbsoluteFile() + "/instances", instaceName);
		if (!workDir.exists())
			workDir.mkdirs();

		forgeDir = new File(getHome().getAbsoluteFile() + "/", "forge");
		if (!forgeDir.exists())
			forgeDir.mkdirs();

	//	Launch.form.progressBar1.setValue(2);

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
		//Launch.form.progressBar1.setValue(3);
		println("Creating the profiles.jsonFile");

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

		//Launch.form.progressBar1.setValue(4);

		if (forgeVersion != "") {
			File mcverDir = new File(mcDir, "versions/" + minecraftVersion);
			if (!mcverDir.exists()) {
				println("Downloading minecraft");
				MinecraftVersionInstaller.installMc(minecraftVersion, this);
			//	Launch.form.progressBar1.setValue(5);
			}
			//Launch.form.progressBar1.setValue(6);
			println("Using forge");
			File forgeInstaller = new File(forgeDir, "forge-" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "-installer.jar");
			if (!forgeInstaller.exists()) {
				try {
					DownloadUtils.downloadFile("http://files.minecraftforge.net/maven/net/minecraftforge/forge/" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "/forge-" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "-installer.jar", forgeDir, forgeInstaller.getName());
					//Launch.form.progressBar1.setValue(6);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			addToClasspath(forgeInstaller);
			File forgeInstallLocation = new File(mcDir, "versions/" + minecraftVersion + "-Forge" + forgeVersion + "-" + minecraftVersion);
			if (forgeInstaller.exists() && !(forgeInstallLocation.exists())) {
				println("Installing forge");
				ForgeInstaller.installForge(mcDir);
			//	Launch.form.progressBar1.setValue(7);
			}
		}

		println("Starting the minecraft launcher");
		//Launch.form.progressBar1.setValue(8);

		try {
			Process proc = Runtime.getRuntime().exec("java -jar " + mcExe.getAbsolutePath() + " -workDir " + mcDir.getAbsolutePath());
			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null)
				print(line);
			int exitVal = proc.waitFor();
			println("Process exitValue: " + exitVal);

			//Getting the launcher ready to run again
			//Launch.form.launchModPackButton.setEnabled(true);
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
		//TODO make a console
	}
}
