package Views;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import Views.PublishersTab.AddAction;
import Views.PublishersTab.DeleteAction;
import Views.PublishersTab.EditAction;
import Views.PublishersTab.MouseAction;

public class GamesTab extends BaseTab {
	private int id;

	private JTextField titleField = new JTextField();
	private JTextField descriptionField = new JTextField();
	private JComboBox<ComboBoxItem> genresComboBox;
	private JComboBox<ComboBoxItem> publishersComboBox;
	
	public GamesTab() {
		super("game");

		panel.setLayout(new GridBagLayout());

		// Title
		JPanel formPanel = new JPanel(new GridBagLayout());
		JLabel titleLabel = new JLabel("Title");
		titleLabel.setMinimumSize(new Dimension(70, titleLabel.getMinimumSize().height));
		titleLabel.setPreferredSize(new Dimension(70, titleLabel.getPreferredSize().height));
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

		// Genres
		JLabel genresLabel = new JLabel("Genres");
		genresLabel.setMinimumSize(new Dimension(70, genresLabel.getMinimumSize().height));
		genresLabel.setPreferredSize(new Dimension(70, genresLabel.getPreferredSize().height));
		gbc.gridx = 0;
		gbc.gridy = 2;
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
		publishersLabel.setMinimumSize(new Dimension(70, publishersLabel.getMinimumSize().height));
		publishersLabel.setPreferredSize(new Dimension(70, publishersLabel.getPreferredSize().height));
		gbc.gridx = 0;
		gbc.gridy = 3;
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
		//addButton.addActionListener(new AddAction());
		buttonsPanel.add(addButton);
		JButton editButton = new JButton("Edit");
		//editButton.addActionListener(new EditAction());
		buttonsPanel.add(editButton);
		JButton deleteButton = new JButton("Delete");
		//deleteButton.addActionListener(new DeleteAction());
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

		//table.addMouseListener(new MouseAction());
	}

	private void clearForm() {
		titleField.setText("");
		descriptionField.setText("");
	}

	@Override
	public void loadData() {
		super.loadData();

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
}
