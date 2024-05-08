package Views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Database.DbConnection;

public class CountriesTab extends BaseTab {
	private int id;

	private JTextField nameField = new JTextField();

	public CountriesTab() {
		super("country");

		panel.setLayout(new GridBagLayout());

		// Name
		JPanel formPanel = new JPanel(new GridBagLayout());
		JLabel nameLabel = new JLabel("Name");
		nameLabel.setMinimumSize(new Dimension(70, nameLabel.getMinimumSize().height));
		nameLabel.setPreferredSize(new Dimension(70, nameLabel.getPreferredSize().height));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 5, 0);
		formPanel.add(nameLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		formPanel.add(nameField, gbc);

		// Buttons
		JPanel buttonsPanel = new JPanel();
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new AddAction());
		buttonsPanel.add(addButton);
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(new EditAction());
		buttonsPanel.add(editButton);
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new DeleteAction());
		buttonsPanel.add(deleteButton);

		// Table
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		panel.add(formPanel, gbc);

		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 5, 0);
		panel.add(buttonsPanel, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		panel.add(tableScroll, gbc);

		table.addMouseListener(new MouseAction());
	}

	private void clearForm() {
		nameField.setText("");
	}

	@Override
	public void loadData() {
		super.loadData();

		table.removeColumn(table.getColumnModel().getColumn(0));
	}

	class AddAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Connection connection = DbConnection.getConnection();
				String query = "INSERT INTO country(NAME) VALUES(?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setString(1, nameField.getText());

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to insert country in DB:");
				System.out.println(ex.getMessage());
			}

			loadData();
			clearForm();
		}
	}

	class EditAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Connection connection = DbConnection.getConnection();
				String query = "UPDATE country SET NAME = ? WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setString(1, nameField.getText());
				statement.setInt(2, id);

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to update country in DB:");
				System.out.println(ex.getMessage());
			}

			loadData();
			clearForm();
		}
	}

	class DeleteAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Connection connection = DbConnection.getConnection();
				String query = "DELETE country WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setInt(1, id);

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to delete country in DB:");
				System.out.println(ex.getMessage());
			}

			loadData();
			clearForm();
		}
	}

	class MouseAction implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
			nameField.setText(table.getModel().getValueAt(row, 1).toString());
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
