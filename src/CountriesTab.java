import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

public class CountriesTab extends BaseTab {
	public CountriesTab() {
		super("country");
		
		panel.setLayout(new GridBagLayout());

		JLabel countriesLabel = new JLabel("Countries");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(countriesLabel, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		panel.add(tableScroll, gbc);
	}
}
