package openlauncher;

import net.minecraft.launcher.Launcher;
import net.minecraft.launcher.MinecraftUserInterface;
import net.minecraft.launcher.SwingUserInterface;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.PasswordAuthentication;
import java.net.Proxy;

/**
 * Created by mark on 25/03/15.
 */
public class FakeMCLauncher extends Launcher {

	public FakeMCLauncher(JFrame frame, File workingDirectory, Proxy proxy, PasswordAuthentication proxyAuth, String[] args, Integer bootstrapVersion) {
		super(frame, workingDirectory, proxy, proxyAuth, args, bootstrapVersion);
		getUserInterface().setVisible(false);
	}


//	private MinecraftUserInterface selectUserInterface(JFrame frame) {
//		return new FakeMCInterface(this, frame);
//	}

}
