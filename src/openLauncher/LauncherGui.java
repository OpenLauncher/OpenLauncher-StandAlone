package openLauncher;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mark on 12/12/14.
 */
public class LauncherGui {
	public JTabbedPane tabbedPane1;
	public JPanel panel1;
	public JTextField textField1;
	public JPasswordField passwordField1;
	public JButton button1;
	public JCheckBox checkBox1;
	public JComboBox comboBox1;
	public JTextField textField2;
	public JTextPane textPane1;

	public static void main(String[] args) {
		LauncherGui launcherGui = new LauncherGui();
		launcherGui.comboBox1.addItem("Creeper Host");
		launcherGui.comboBox1.addItem("Creeper Host 2");
		launcherGui.textPane1.setText("This is some test news it does not load it from the net at the moment. This is so i can check some things and take some screen shots of it in use.");
		JFrame frame = new JFrame("LauncherGui");
		frame.setContentPane(launcherGui.panel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
