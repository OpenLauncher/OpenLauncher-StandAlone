package openlauncher;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main {

	static File mcDir;
	static File mcExe;
	static File workDir;
	static String instaceName = "test";

	public static void main(String[] args) {
		System.out.println("Starting the open Launcher");
		System.out.println(getHome().getAbsolutePath());

		if(!getHome().exists()){
			System.out.println("Creating openLauncher folder at:" + getHome().getAbsolutePath());
			getHome().mkdirs();
		}

		mcDir = new File(getHome().getAbsoluteFile() + "/", "Minecraft");
		if(!mcDir.exists())
			mcDir.mkdirs();

		workDir = new File(getHome().getAbsoluteFile() + "/", instaceName);
		if(!workDir.exists())
			workDir.mkdirs();

		if(isWindows()){
			//TODO handle windows .exe -Cant do now because on mac
		} else {
			//We will use a jar now
			mcExe = new File(mcDir, "MinecraftLauncher.jar");
			if(!mcExe.exists()){
				//We need to download the minecraft jar file
				System.out.println("Downloading the minecraft launcher...");
				try{
					URL website = new URL("https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar");
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					FileOutputStream fos = new FileOutputStream(mcExe);
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					System.out.println("Minecraft downloaded successfully!");
				} catch (MalformedURLException e){
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//TODO make the json file here to select what pack will be launched.

			try{
				Process proc = Runtime.getRuntime().exec("java -jar " + mcExe.getAbsolutePath() + " -workDir " + mcDir.getAbsolutePath());
				proc.waitFor();
				// Then retreive the process output
				InputStream in = proc.getInputStream();
				InputStream err = proc.getErrorStream();

				byte b[]=new byte[in.available()];
				in.read(b,0,b.length);
				System.out.println(new String(b));

				byte c[]=new byte[err.available()];
				err.read(c,0,c.length);
				System.out.println(new String(c));
			}catch (IOException e){

			} catch (InterruptedException e) {
				e.printStackTrace();
			}


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
}
