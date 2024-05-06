package Views;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamesTab extends BaseTab {
	public GamesTab() {
		super("game");
		
		panel.setLayout(new GridBagLayout());
		
		JPanel gamesFormPanel = new JPanel(new GridLayout());
		gamesFormPanel.add(new JLabel("Name"));
		gamesFormPanel.add(new JTextField());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(gamesFormPanel, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		panel.add(tableScroll, gbc);
	}
}
