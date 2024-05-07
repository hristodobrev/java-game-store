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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Database.DbConnection;
import Utils.ComboBoxItem;

public class PublishersTab extends BaseTab {
	private int id;

	private JTextField nameField = new JTextField();
	private JTextField descriptionField = new JTextField();
	private JComboBox<ComboBoxItem> countriesComboBox;

	public PublishersTab() {
		super("publisher");

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

		// Description
		JLabel descriptionLabel = new JLabel("Description");
		descriptionLabel.setMinimumSize(new Dimension(70, descriptionLabel.getMinimumSize().height));
		descriptionLabel.setPreferredSize(new Dimension(70, descriptionLabel.getPreferredSize().height));
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(descriptionLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 0, 0, 0);
		formPanel.add(descriptionField, gbc);

		// Countries
		JLabel countriesLabel = new JLabel("Countries");
		countriesLabel.setMinimumSize(new Dimension(70, countriesLabel.getMinimumSize().height));
		countriesLabel.setPreferredSize(new Dimension(70, countriesLabel.getPreferredSize().height));
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(countriesLabel, gbc);

		countriesComboBox = new JComboBox<ComboBoxItem>(getCountries());
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(5, 0, 5, 0);
		formPanel.add(countriesComboBox, gbc);

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
		descriptionField.setText("");
	}

	@Override
	public void loadData() {
		super.loadData();

		DefaultComboBoxModel<ComboBoxItem> model = new DefaultComboBoxModel<ComboBoxItem>(getCountries());
		countriesComboBox.setModel(model);
	}

	private ComboBoxItem[] getCountries() {
		List<ComboBoxItem> countries = new ArrayList<ComboBoxItem>();

		try {
			Connection connection = DbConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM country");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				countries.add(new ComboBoxItem(resultSet.getInt("id"), resultSet.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return countries.toArray(new ComboBoxItem[0]);
	}

	class AddAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Connection connection = DbConnection.getConnection();
				String query = "INSERT INTO publisher(NAME, DESCRIPTION, COUNTRY_ID) VALUES(?,?,?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setString(1, nameField.getText());
				statement.setString(2, descriptionField.getText());
				statement.setInt(3, ((ComboBoxItem) countriesComboBox.getSelectedItem()).getId());

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to insert publisher in DB:");
				System.out.println(ex.getMessage());
			}

			loadData();
			clearForm();
		}
	}

	private void setCountryCombox(int countryId) {
		for (int i = 0; i < countriesComboBox.getItemCount(); i++) {
			ComboBoxItem country = countriesComboBox.getItemAt(i);
			if (country.getId() == countryId) {
				countriesComboBox.setSelectedIndex(i);
				break;
			}
		}
	}

	class EditAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Connection connection = DbConnection.getConnection();
				String query = "UPDATE publisher SET NAME = ?, DESCRIPTION = ?, COUNTRY_ID = ? WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setString(1, nameField.getText());
				statement.setString(2, descriptionField.getText());
				statement.setInt(3, ((ComboBoxItem) countriesComboBox.getSelectedItem()).getId());
				statement.setInt(4, id);

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to update publisher in DB:");
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
				String query = "DELETE publisher WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setInt(1, id);

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to publisher genre in DB:");
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
			id = Integer.parseInt(table.getValueAt(row, 0).toString());
			nameField.setText(table.getValueAt(row, 1).toString());
			descriptionField.setText(table.getValueAt(row, 2).toString());
			int countryId = Integer.parseInt(table.getValueAt(row, 3).toString());
			setCountryCombox(countryId);
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
