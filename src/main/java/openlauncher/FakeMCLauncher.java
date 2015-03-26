package openlauncher;

import net.minecraft.launcher.Launcher;

import javax.swing.*;
import java.io.File;
import java.net.PasswordAuthentication;
import java.net.Proxy;

public class FakeMCLauncher extends Launcher {

	public FakeMCLauncher(JFrame frame, File workingDirectory, Proxy proxy, PasswordAuthentication proxyAuth, String[] args, Integer bootstrapVersion) {
		super(frame, workingDirectory, proxy, proxyAuth, args, bootstrapVersion);
		getUserInterface().setVisible(false);
	}

}
