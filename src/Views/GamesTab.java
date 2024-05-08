package Views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.Date;
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

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import Database.DbConnection;
import Utils.ComboBoxItem;
import Views.PublishersTab.AddAction;
import Views.PublishersTab.DeleteAction;
import Views.PublishersTab.EditAction;
import Views.PublishersTab.MouseAction;

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

		// Title
		JPanel formPanel = new JPanel(new GridBagLayout());
		JLabel titleLabel = new JLabel("Title");
		titleLabel.setMinimumSize(new Dimension(100, titleLabel.getMinimumSize().height));
		titleLabel.setPreferredSize(new Dimension(100, titleLabel.getPreferredSize().height));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 5, 0);
		formPanel.add(titleLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		formPanel.add(titleField, gbc);

		// Description
		JLabel descriptionLabel = new JLabel("Description");
		descriptionLabel.setMinimumSize(new Dimension(100, descriptionLabel.getMinimumSize().height));
		descriptionLabel.setPreferredSize(new Dimension(100, descriptionLabel.getPreferredSize().height));
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(descriptionLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 0, 5, 0);
		formPanel.add(descriptionField, gbc);

		// Release Date
		JLabel releaseDateLabel = new JLabel("Release Date");
		releaseDateLabel.setMinimumSize(new Dimension(100, releaseDateLabel.getMinimumSize().height));
		releaseDateLabel.setPreferredSize(new Dimension(100, releaseDateLabel.getPreferredSize().height));
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(releaseDateLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 0, 0, 0);
		releaseDateChooser.setDateFormatString("yyyy-MM-dd");
		formPanel.add(releaseDateChooser, gbc);
		
		// Genres
		JLabel genresLabel = new JLabel("Genres");
		genresLabel.setMinimumSize(new Dimension(100, genresLabel.getMinimumSize().height));
		genresLabel.setPreferredSize(new Dimension(100, genresLabel.getPreferredSize().height));
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(genresLabel, gbc);

		genresComboBox = new JComboBox<ComboBoxItem>(getGenres());
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(5, 0, 5, 0);
		formPanel.add(genresComboBox, gbc);

		// Publishers
		JLabel publishersLabel = new JLabel("Publishers");
		publishersLabel.setMinimumSize(new Dimension(100, publishersLabel.getMinimumSize().height));
		publishersLabel.setPreferredSize(new Dimension(100, publishersLabel.getPreferredSize().height));
		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(publishersLabel, gbc);

		publishersComboBox = new JComboBox<ComboBoxItem>(getPublishers());
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(5, 0, 5, 0);
		formPanel.add(publishersComboBox, gbc);

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
		titleField.setText("");
		descriptionField.setText("");
	}

	@Override
	public void loadData() {
		super.loadData();

		table.removeColumn(table.getColumnModel().getColumn(0));
		table.removeColumn(table.getColumnModel().getColumn(3));
		table.removeColumn(table.getColumnModel().getColumn(4));
		genresComboBox.setModel(new DefaultComboBoxModel<ComboBoxItem>(getGenres()));
		publishersComboBox.setModel(new DefaultComboBoxModel<ComboBoxItem>(getPublishers()));
	}

	private ComboBoxItem[] getGenres() {
		List<ComboBoxItem> genres = new ArrayList<ComboBoxItem>();

		try {
			Connection connection = DbConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM genre");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				genres.add(new ComboBoxItem(resultSet.getInt("id"), resultSet.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return genres.toArray(new ComboBoxItem[0]);
	}

	private ComboBoxItem[] getPublishers() {
		List<ComboBoxItem> publishers = new ArrayList<ComboBoxItem>();

		try {
			Connection connection = DbConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM publisher");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				publishers.add(new ComboBoxItem(resultSet.getInt("id"), resultSet.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return publishers.toArray(new ComboBoxItem[0]);
	}

	private void setPublishersCombox(int publisherId) {
		for (int i = 0; i < publishersComboBox.getItemCount(); i++) {
			ComboBoxItem publisher = publishersComboBox.getItemAt(i);
			if (publisher.getId() == publisherId) {
				publishersComboBox.setSelectedIndex(i);
				break;
			}
		}
	}

	private void setGenresCombox(int genreId) {
		for (int i = 0; i < genresComboBox.getItemCount(); i++) {
			ComboBoxItem genre = genresComboBox.getItemAt(i);
			if (genre.getId() == genreId) {
				genresComboBox.setSelectedIndex(i);
				break;
			}
		}
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
				String query = "DELETE publisher WHERE id = ?";
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
			setGenresCombox(genreId);
			int publisherId = Integer.parseInt(table.getModel().getValueAt(row, 6).toString());
			setPublishersCombox(publisherId);
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
