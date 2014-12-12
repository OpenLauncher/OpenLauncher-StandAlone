package openLauncher;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mark on 12/12/14.
 */
public class LauncherGui {
	public JTabbedPane tabbedPane1;
	public JPanel panel1;

	public static void main(String[] args) {
		JFrame frame = new JFrame("LauncherGui");
		frame.setContentPane(new LauncherGui().panel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
