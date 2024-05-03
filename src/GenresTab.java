import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GenresTab extends BaseTab {
	private int id;

	private JTextField nameField = new JTextField();
	private JTextField descriptionField = new JTextField();
	
	public GenresTab() {
		super("genre");

		panel.setLayout(new GridBagLayout());

		JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		JLabel nameLabel = new JLabel("Name");
		JLabel descriptionLabel = new JLabel("Description");
		formPanel.add(nameLabel);
		formPanel.add(nameField);
		formPanel.add(descriptionLabel);
		formPanel.add(descriptionField);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		panel.add(formPanel, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		panel.add(tableScroll, gbc);

		table.addMouseListener(new MouseAction());
	}

	class MouseAction implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			id = Integer.parseInt(table.getValueAt(row, 0).toString());
			nameField.setText(table.getValueAt(row, 1).toString());
			descriptionField.setText(table.getValueAt(row, 2).toString());
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}
