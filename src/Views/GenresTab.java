package Views;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Database.DbConnection;

public class GenresTab extends BaseTab {
	private int id;

	private JTextField nameField = new JTextField();
	private JTextField descriptionField = new JTextField();

	public GenresTab() {
		super("genre");

		panel.setLayout(new GridBagLayout());

		JPanel formPanel = new JPanel(new GridBagLayout());

		// Name
		addLabelToPanel("Name", formPanel, 0, 0);
		formPanel.add(nameField, getRightGBC(1, 0));
		
		// Description
		addLabelToPanel("Description", formPanel, 0, 1);
		formPanel.add(descriptionField, getRightGBC(1, 1));

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
	}
	
	private boolean validateFields() {
		if(nameField.getText().isBlank()) {
			JOptionPane.showMessageDialog(null, "Name is required!");
			return false;
		}

		if(descriptionField.getText().isBlank()) {
			JOptionPane.showMessageDialog(null, "Description is required!");
			return false;
		}
		
		return true;
	}

	class AddAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!validateFields()) {
				return;
			}
			
			try {
				Connection connection = DbConnection.getConnection();
				String query = "INSERT INTO genre(NAME, DESCRIPTION) VALUES(?,?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setString(1, nameField.getText());
				statement.setString(2, descriptionField.getText());

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to insert genre in DB:");
				System.out.println(ex.getMessage());
			}

			loadData();
			clearForm();
		}
	}

	class EditAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!validateFields()) {
				return;
			}
			
			try {
				Connection connection = DbConnection.getConnection();
				String query = "UPDATE genre SET NAME = ?, DESCRIPTION = ? WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setString(1, nameField.getText());
				statement.setString(2, descriptionField.getText());
				statement.setInt(3, id);

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to update genre in DB:");
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
				String query = "DELETE genre WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setInt(1, id);

				statement.execute();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "There is a game using this genre! Delete all games using this genre first!");
				System.out.println("Error while trying to delete genre in DB:");
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
