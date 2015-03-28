package openlauncher.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import openlauncher.Launch;
import openlauncher.ModPackInstance;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LauncherForm {
	public JPanel panel1;
	public JButton launchModPackButton;
	public JProgressBar progressBar1;
	public JList packList;
	public JTextArea textLog;
	public JScrollBar scrollBar1;
	public static DefaultListModel packListString = new DefaultListModel();

	private final Font MONOSPACED = new Font("Monospaced", 0, 12);

	public LauncherForm() {
		$$$setupUI$$$();
		if (launchModPackButton != null)
			launchModPackButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Thread thread = new Thread() {
						public void run() {
							if (!packList.isSelectionEmpty()) {
								//ModPackInstance pack = Launch.packMap.get(packList.getSelectedValue());
								//Launch.main.launch(pack.getInstanceName(), pack.getForgeVersion(), pack.getMinecraftVersion());
								//launchModPackButton.setEnabled(false);
							}
						}
					};
					thread.start();
				}
			});
//		packList.addPropertyChangeListener(new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				if(!packList.isSelectionEmpty()) {
//					ModPack pack = Launch.packMap.get(packList.getSelectedValue());
//					textLog.setText(pack.getText());
//				} else {
//					textLog.setText("Please choose a pack on the left to play");
//				}
//			}
//		});
		Launch.form = this;
	}


	public void openGui() {
		JFrame frame = new JFrame("OpenLauncher");
		frame.setContentPane(panel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createUIComponents();
		textLog.setLineWrap(true);
		textLog.setEditable(false);
		textLog.setFont(MONOSPACED);
		((DefaultCaret) textLog.getCaret()).setUpdatePolicy(1);

//		PrintStream out = new PrintStream(new TextAreaOutputStream(textLog));
//		System.setOut(out);
//		System.setErr(out);

		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		Launch.form = this;
	}

	private void createUIComponents() {
		Launch.form = this;
		//packList = new JList(Launch.packMap.keySet().toArray());
		packList = new JList(packListString);
		packList.setSelectedIndex(0);
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(854, 480), null, new Dimension(854, 480), 0, false));
		final Spacer spacer1 = new Spacer();
		panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel2.add(panel3, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		launchModPackButton = new JButton();
		launchModPackButton.setText("Launch Mod Pack");
		panel4.add(launchModPackButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		panel2.add(packList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
		textLog = new JTextArea();
		textLog.setEditable(false);
		textLog.setText("");
		panel2.add(textLog, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
		progressBar1 = new JProgressBar();
		progressBar1.setMaximum(8);
		panel2.add(progressBar1, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		scrollBar1 = new JScrollBar();
		panel2.add(scrollBar1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel1;
	}
}
