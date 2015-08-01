package openlauncher;


import openlauncher.gui.LauncherForm;
import openlauncher.minecraft.ForgeInstaller;
import openlauncher.minecraft.MinecraftVersionInstaller;
import openlauncher.minecraft.profileCreator;
import openlauncher.modPack.PackLoader;
import openlauncher.util.DownloadUtils;
import openlauncher.util.Logger;
import openlauncher.util.OperatingSystem;

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

public class OpenLauncher {

	public static Logger logger;
	static File mcDir;
	static File mcExe;
	static File mcLauncher;
	static File workDir;
	static File forgeDir;
	static File libsDir;
	private final Font MONOSPACED = new Font("Monospaced", 0, 12);
	String instanceName;
	String forgeVersion;
	String minecraftVersion;
	private StringBuilder outputBuffer = new StringBuilder();

	public static File getHome() {
		if (isLinux()) {
			try {
				return new File(OpenLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI()
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

	public void start(LauncherForm form) {
		Main.form = form;
		logger = new Logger("OpenLauncher-Main");
		logger.setVerboseity(Main.dev);
		if (Main.offlineDev) {
			logger.warn("Running in offline mode");
		}
		logger.info("Starting the openLauncher");

		libsDir = new File(getHome().getAbsoluteFile() + "/", "libs");
		if (!libsDir.exists())
			libsDir.mkdirs();

		File commons = new File(libsDir, "commons-io-2.4.jar");
		if (!commons.exists()) {
			logger.info("Downloading commons-io-2.4.jar");
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
			logger.info("Downloading javax.json-1.0.4.jar");
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
			logger.info("Downloading gson-2.3.1.jar");
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
		if (!Main.offlineDev) {
			try {
				logger.info("Downloading the pack list");
				DownloadUtils.downloadFile("http://www.creeperrepo.net/OpenLauncher/launcher/packs.json", getHome(), "packs.json", "");//TODO md5
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		if (packsJson.exists() || Main.offlineDev) {
			try {
				logger.debug("Reading the pack list");
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

		logger.info("Starting " + instaceName);
		Main.form.progressBar1.setValue(1);
		Main.form.progressBar1.setString("Starting " + instaceName);


		logger.debug(getHome().getAbsolutePath());

		if (!getHome().exists()) {
			logger.debug("Creating openLauncher folder at:" + getHome().getAbsolutePath());
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

		Main.form.progressBar1.setValue(2);
		Main.form.progressBar1.setString("Checking/Downloading Minecraft launcher");

		//We will use a jar now
		mcExe = new File(mcDir, "MinecraftLauncher.jar");
		if (!mcExe.exists()) {
			//We need to download the minecraft jar file
			logger.info("Downloading the minecraft launcher bootstrap...");
			try {
				DownloadUtils.downloadFile("https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar", mcDir, "MinecraftLauncher.jar", "");//TODO md5
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		Main.form.progressBar1.setValue(3);
		logger.debug("Creating the profiles.jsonFile");
		Main.form.progressBar1.setString("Injecting custom profiles into minecraft");

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

		Main.form.progressBar1.setValue(4);
		Main.form.progressBar1.setString("Downloading minecraft and forge");

		if (forgeVersion != "") {
			File mcverDir = new File(mcDir, "versions/" + minecraftVersion);
			if (!mcverDir.exists()) {
				logger.info("Downloading minecraft");
				MinecraftVersionInstaller.installMc(minecraftVersion, this);
				Main.form.progressBar1.setValue(5);
			}
			Main.form.progressBar1.setValue(6);
			logger.debug("Using forge");
			File forgeInstaller = new File(forgeDir, "forge-" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "-installer.jar");
			if (!forgeInstaller.exists()) {
				try {
					DownloadUtils.downloadFile("http://files.minecraftforge.net/maven/net/minecraftforge/forge/" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "/forge-" + minecraftVersion + "-" + forgeVersion + "-" + minecraftVersion + "-installer.jar", forgeDir, forgeInstaller.getName(), "");//TODO md5
					Main.form.progressBar1.setValue(6);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			addToClasspath(forgeInstaller);
			File forgeInstallLocation = new File(mcDir, "versions/" + minecraftVersion + "-Forge" + forgeVersion + "-" + minecraftVersion);
			if (forgeInstaller.exists() && !(forgeInstallLocation.exists())) {
				logger.info("Installing forge");
				ForgeInstaller.installForge(mcDir);
				Main.form.progressBar1.setValue(7);
			}
		}

		logger.info("Starting the minecraft launcher");
		Main.form.progressBar1.setValue(8);
		Main.form.progressBar1.setString("Running minecraft...");

		Logger mcLogger = new Logger("Minecraft");

		try {
			Process proc = Runtime.getRuntime().exec("java -jar " + mcExe.getAbsolutePath() + " -workDir " + mcDir.getAbsolutePath());
			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null)
				mcLogger.info(line);
			int exitVal = proc.waitFor();
			mcLogger.info("Process exitValue: " + exitVal);

			//Getting the launcher ready to run again
			Main.form.launchModPackButton.setEnabled(true);
			Main.form.progressBar1.setString("Waiting...");
			Main.form.progressBar1.setValue(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Credit to https://github.com/Slowpoke101/FTBLaunch/blob/master/src/main/java/net/ftb/workers/AuthlibDLWorker.java
	 */
	public boolean addToClasspath(File file) {
		logger.debug("Loading external library " + file.getName() + " to classpath");
		try {
			if (file.exists()) {
				addURL(file.toURI().toURL());
			} else {
				logger.error("Error loading jar");
			}
		} catch (Throwable t) {
			if (t.getMessage() != null) {
				logger.error(t.getMessage());
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
				logger.error(t.getMessage());
			}
			throw new IOException("Error, could not add URL to system classloader");
		}
	}

	public File getMcDir() {
		return mcDir;
	}
}
