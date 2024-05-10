package Views;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import Database.DbConnection;
import Utils.ComboBoxItem;

public class GamesTab extends BaseTab {
	private int id;

	private JTextField titleField = new JTextField();
	private JTextField descriptionField = new JTextField();
	JDateChooser releaseDateChooser = new JDateChooser();
	private JComboBox<ComboBoxItem> genresComboBox;
	private JComboBox<ComboBoxItem> publishersComboBox;

	public GamesTab() {
		super("game", "SELECT g.id, g.title, g.description, g.release_date as `release date`, gr.id as `genre_id`, gr.name as `genre`, p.id as `publisher_id`, p.name as `publisher` FROM game g INNER JOIN genre gr ON gr.id = g.genre_id INNER JOIN publisher p ON p.id = g.publisher_id");

		panel.setLayout(new GridBagLayout());

		JPanel formPanel = new JPanel(new GridBagLayout());
		
		// Title
		addLabelToPanel("Title", formPanel, 0, 0);
		formPanel.add(titleField, getRightGBC(1, 0));

		// Description
		addLabelToPanel("Description", formPanel, 0, 1);
		formPanel.add(descriptionField, getRightGBC(1, 1));

		// Release Date
		addLabelToPanel("Release Date", formPanel, 0, 2);
		releaseDateChooser.setDateFormatString("yyyy-MM-dd");
		formPanel.add(releaseDateChooser, getRightGBC(1, 2));
		
		// Genres
		addLabelToPanel("Genres", formPanel, 0, 3);
		genresComboBox = new JComboBox<ComboBoxItem>(getComboBox("genre"));
		formPanel.add(genresComboBox, getRightGBC(1, 3));

		// Publishers
		addLabelToPanel("Publishers", formPanel, 0, 4);
		publishersComboBox = new JComboBox<ComboBoxItem>(getComboBox("publisher"));
		formPanel.add(publishersComboBox, getRightGBC(1, 4));

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
		titleField.setText("");
		descriptionField.setText("");
	}

	@Override
	public void loadData() {
		super.loadData();

		table.removeColumn(table.getColumnModel().getColumn(0));
		table.removeColumn(table.getColumnModel().getColumn(3));
		table.removeColumn(table.getColumnModel().getColumn(4));
		genresComboBox.setModel(new DefaultComboBoxModel<ComboBoxItem>(getComboBox("genre")));
		publishersComboBox.setModel(new DefaultComboBoxModel<ComboBoxItem>(getComboBox("publisher")));
	}

	class AddAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Connection connection = DbConnection.getConnection();
				String query = "INSERT INTO game(TITLE, DESCRIPTION, RELEASE_DATE, PUBLISHER_ID, GENRE_ID) VALUES(?,?,?,?,?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setString(1, titleField.getText());
				statement.setString(2, descriptionField.getText());
				statement.setDate(3, new Date(releaseDateChooser.getDate().getTime()));
				statement.setInt(4, ((ComboBoxItem) publishersComboBox.getSelectedItem()).getId());
				statement.setInt(5, ((ComboBoxItem) genresComboBox.getSelectedItem()).getId());

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to insert game in DB:");
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
				String query = "UPDATE game SET TITLE = ?, DESCRIPTION = ?, RELEASE_DATE = ?, PUBLISHER_ID = ?, GENRE_ID = ? WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setString(1, titleField.getText());
				statement.setString(2, descriptionField.getText());
				statement.setDate(3, new Date(releaseDateChooser.getDate().getTime()));
				statement.setInt(4, ((ComboBoxItem) publishersComboBox.getSelectedItem()).getId());
				statement.setInt(5, ((ComboBoxItem) genresComboBox.getSelectedItem()).getId());
				statement.setInt(6, id);

				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to update game in DB:");
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
				String query = "DELETE game WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement = connection.prepareStatement(query);
				statement.setInt(1, id);
				
				statement.execute();
			} catch (SQLException ex) {
				System.out.println("Error while trying to delete game in DB:");
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
			titleField.setText(table.getModel().getValueAt(row, 1).toString());
			descriptionField.setText(table.getModel().getValueAt(row, 2).toString());
			releaseDateChooser.setDate(Date.valueOf(table.getModel().getValueAt(row, 3).toString()));
			int genreId = Integer.parseInt(table.getModel().getValueAt(row, 4).toString());
			ComboBoxItem.setSelectedId(genreId, genresComboBox);
			int publisherId = Integer.parseInt(table.getModel().getValueAt(row, 6).toString());
			ComboBoxItem.setSelectedId(publisherId, publishersComboBox);		}

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
