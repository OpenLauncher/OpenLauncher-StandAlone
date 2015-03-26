package openlauncher.gui;

import openlauncher.Launch;
import openlauncher.ModPack;

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

	private final Font MONOSPACED = new Font("Monospaced", 0, 12);

	public LauncherForm() {
		launchModPackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(){
					public void run(){
						if(!packList.isSelectionEmpty()){
							ModPack pack = Launch.packMap.get(packList.getSelectedValue());
							Launch.main.launch(pack.getInstanceName(), pack.getForgeVersion(), pack.getMinecraftVersion());
							launchModPackButton.setEnabled(false);
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
	}

	public void openGui() {
		JFrame frame = new JFrame("OpenLauncher");
		frame.setContentPane(new LauncherForm().panel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.textLog.setLineWrap(true);
		this.textLog.setEditable(false);
		this.textLog.setFont(MONOSPACED);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);


	}

	private void createUIComponents() {
		packList = new JList(Launch.packMap.keySet().toArray());
		textLog = new JTextArea();
		textLog.setLineWrap(true);
		textLog.setEditable(false);
		textLog.setFont(MONOSPACED);
		((DefaultCaret)textLog.getCaret()).setUpdatePolicy(1);
		Launch.form = this;
		packList.setSelectedIndex(0);
	}
}
