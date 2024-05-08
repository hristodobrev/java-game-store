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
import java.text.SimpleDateFormat;
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
import Utils.GameStoreTableModel;
import Views.PublishersTab.AddAction;
import Views.PublishersTab.DeleteAction;
import Views.PublishersTab.EditAction;
import Views.PublishersTab.MouseAction;

public class StoreTab extends BaseTab {
	private int id;

	private JTextField titleField = new JTextField();
	private JTextField descriptionField = new JTextField();
	JDateChooser releaseDateChooser = new JDateChooser();
	private JComboBox<ComboBoxItem> genresComboBox;
	private JComboBox<ComboBoxItem> publishersComboBox;

	public StoreTab() {
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

		genresComboBox = new JComboBox<ComboBoxItem>(getComboBox("genre"));
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

		publishersComboBox = new JComboBox<ComboBoxItem>(getComboBox("publisher"));
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(5, 0, 5, 0);
		formPanel.add(publishersComboBox, gbc);

		// Buttons
		JPanel buttonsPanel = new JPanel();
		JButton clearButton = new JButton("Clear Form");
		clearButton.addActionListener(new ClearAction());
		buttonsPanel.add(clearButton);
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new SearchAction());
		buttonsPanel.add(searchButton);

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
	}

	private void clearForm() {
		titleField.setText("");
		descriptionField.setText("");
		releaseDateChooser.setDate(null);
		ComboBoxItem.setSelectedId(0, genresComboBox);
		ComboBoxItem.setSelectedId(0, publishersComboBox);
	}

	@Override
	public void loadData() {
	    Connection connection = DbConnection.getConnection();
	    try {
	        String query = this.query + " WHERE 1=1";
	        List<Object> parameters = new ArrayList<>();
	        
	        if(!titleField.getText().isBlank()) {
	            query += " AND upper(g.title) LIKE ?";
	            parameters.add("%" + titleField.getText().toUpperCase() + "%");
	        }
	        
	        if(!descriptionField.getText().isBlank()) {
	            query += " AND upper(g.description) LIKE ?";
	            parameters.add("%" + descriptionField.getText().toUpperCase() + "%");
	        }
	        
	        if(releaseDateChooser.getDate() != null) {
	            query += " AND g.release_date = ?";
	            parameters.add(new Date(releaseDateChooser.getDate().getTime()));
	        }
	        
	        int publisherId = ((ComboBoxItem) publishersComboBox.getSelectedItem()).getId();
	        if(publisherId != 0) {
	            query += " AND g.publisher_id = ?";
	            parameters.add(publisherId);
	        }
	        
	        int genreId = ((ComboBoxItem) genresComboBox.getSelectedItem()).getId();
	        if(genreId != 0) {
	            query += " AND g.genre_id = ?";
	            parameters.add(genreId);
	        }

	        PreparedStatement statement = connection.prepareStatement(query);
	        
	        // Bind parameters
	        int parameterIndex = 1;
	        for (Object parameter : parameters) {
	            if (parameter instanceof Date) {
	                statement.setDate(parameterIndex++, (Date) parameter);
	            } else {
	                statement.setString(parameterIndex++, (String) parameter);
	            }
	        }
	        
	        ResultSet result = statement.executeQuery();

	        table.setModel(new GameStoreTableModel(result));
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    table.removeColumn(table.getColumnModel().getColumn(0));
	    table.removeColumn(table.getColumnModel().getColumn(3));
	    table.removeColumn(table.getColumnModel().getColumn(4));
	}

	class ClearAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clearForm();
		}
	}
	
	class SearchAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			loadData();
		}
	}
}
