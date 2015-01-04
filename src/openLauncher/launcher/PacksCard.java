package openLauncher.launcher;

import javax.swing.*;
import java.awt.*;

public class PacksCard extends CollapsiblePanel {
	private static final long serialVersionUID = -2617283435728223314L;
	private final JTextArea descArea = new JTextArea();
	private final JButton newInstanceButton = new JButton("Install");
	private final JButton createServerButton = new JButton("Create Server");
	private final JButton websiteButton = new JButton("Website");
	private final JButton modsButton = new JButton("Mods");
	private final JPanel actionsPanel = new JPanel(new BorderLayout());
	private final JLabel packName = new JLabel();
	private final JSplitPane splitter = new JSplitPane();
	private final GridBagConstraints gbc = new GridBagConstraints();
	private final Pack pack;

	public PacksCard(final Pack pack) {
		super(pack);
		this.pack = pack;

		//this.splitter.setLeftComponent(new PackImagePanel(pack));
		this.splitter.setRightComponent(this.actionsPanel);
		this.splitter.setEnabled(false);

		JPanel abPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		packName.setText(pack.getName());
		Font boldFont = new Font(packName.getFont().getFontName(), Font.BOLD, packName.getFont().getSize());
		packName.setFont(boldFont);
		abPanel.add(packName);
		abPanel.add(this.newInstanceButton);
		abPanel.add(this.createServerButton);
		abPanel.add(this.websiteButton);
		abPanel.add(this.modsButton);

		this.descArea.setText(pack.getDescription());
		this.descArea.setLineWrap(true);
		this.descArea.setEditable(false);
		this.descArea.setHighlighter(null);
		this.descArea.setWrapStyleWord(true);

		this.actionsPanel.add(new JScrollPane(this.descArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane
				.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		this.actionsPanel.add(abPanel, BorderLayout.SOUTH);
		this.actionsPanel.setPreferredSize(new Dimension(this.actionsPanel.getPreferredSize().width, 180));

		this.getContentPane().add(this.splitter);

			this.modsButton.setVisible(false);


			this.createServerButton.setVisible(false);
	}

	public Pack getPack() {
		return this.pack;
	}
}
