package openlauncher;


import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main extends JFrame {

	static File mcDir;
	static File mcExe;
	static File mcLauncher;
	static File workDir;
	static String instaceName = "test";

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
		((DefaultCaret)this.textArea.getCaret()).setUpdatePolicy(1);

		this.scrollPane = new JScrollPane(this.textArea);
		this.scrollPane.setBorder(null);
		this.scrollPane.setVerticalScrollBarPolicy(22);

		add(this.scrollPane);
		setLocationRelativeTo(null);
		setVisible(true);

		println("Starting the openLauncher");
	}

	public void start(){
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

			//We will use a jar now
			mcExe = new File(mcDir, "MinecraftLauncher.jar");
			if (!mcExe.exists()) {
				//We need to download the minecraft jar file
				println("Downloading the minecraft launcher bootstrap...");
				try {
					URL website = new URL("https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar");
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					FileOutputStream fos = new FileOutputStream(mcExe);
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					println("Minecraft bootstrap downloaded successfully!");
				} catch (MalformedURLException e) {
					e.printStackTrace();
					System.exit(-1);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.exit(-1);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}

			addToClasspath(mcExe);

			mcBootstrap.downloadLauncher(this);

			mcLauncher = new File(mcDir, "launcher.jar");

			addToClasspath(mcLauncher);

			try {
				profileCreator.createProfile(mcDir, "1.6.4", "testing");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.exit(-1);
			}

			try {
				Process proc = Runtime.getRuntime().exec("java -jar " + mcExe.getAbsolutePath() + " -workDir " + mcDir.getAbsolutePath());
				proc.waitFor();
				// Then retreive the process output
				InputStream in = proc.getInputStream();
				InputStream err = proc.getErrorStream();

				byte b[] = new byte[in.available()];
				in.read(b, 0, b.length);
				println(new String(b));

				byte c[] = new byte[err.available()];
				err.read(c, 0, c.length);
				println(new String(c));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
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
		try
		{
			document.insertString(document.getLength(), string, null);
		}
		catch (BadLocationException ignored) {
		}
		if (shouldScroll)
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollBar.setValue(2147483647);
				}
			});
	}
}
