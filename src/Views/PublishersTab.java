package Views;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
		super("publisher", "SELECT p.id, p.name, p.description, c.id as country_id, c.name as `country name` FROM publisher p INNER JOIN country c on p.country_id = c.id");

		panel.setLayout(new GridBagLayout());

		JPanel formPanel = new JPanel(new GridBagLayout());

		// Name
		addLabelToPanel("Name", formPanel, 0, 0);
		addTextFieldToPanel(nameField, formPanel, 1, 0);

		// Description
		addLabelToPanel("Description", formPanel, 0, 1);
		addTextFieldToPanel(descriptionField, formPanel, 1, 1);

		// Countries
		addLabelToPanel("Countries", formPanel, 0, 2);
		countriesComboBox = new JComboBox<ComboBoxItem>(getComboBox("country"));
		addComboBoxToPanel(countriesComboBox, formPanel, 1, 2);

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
		setupPanel(formPanel, buttonsPanel);
		
		table.addMouseListener(new MouseAction());
	}

	private void clearForm() {
		nameField.setText("");
		descriptionField.setText("");
	}

	@Override
	public void loadData() {
		super.loadData();

		table.removeColumn(table.getColumnModel().getColumn(0));
		table.removeColumn(table.getColumnModel().getColumn(2));
		DefaultComboBoxModel<ComboBoxItem> model = new DefaultComboBoxModel<ComboBoxItem>(getComboBox("country"));
		countriesComboBox.setModel(model);
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
				System.out.println("Error while trying to delete publisher in DB:");
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
			descriptionField.setText(table.getModel().getValueAt(row, 2).toString());
			int countryId = Integer.parseInt(table.getModel().getValueAt(row, 3).toString());
			ComboBoxItem.setSelectedId(countryId, countriesComboBox);
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
