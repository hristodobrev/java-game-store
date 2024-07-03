package Views;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import Database.DbConnection;
import Utils.ComboBoxItem;
import Utils.GameStoreTableModel;

public class StoreTab extends BaseTab {
	private JTextField titleField = new JTextField();
	private JTextField descriptionField = new JTextField();
	JDateChooser releaseDateChooser = new JDateChooser();
	private JComboBox<ComboBoxItem> genresComboBox;
	private JTextField genreField = new JTextField();
	private JComboBox<ComboBoxItem> publishersComboBox;
	private JTextField publisherField = new JTextField();

	public StoreTab() {
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
		genresComboBox = new JComboBox<ComboBoxItem>(getComboBox("genre", true));

		JPanel genrePanel = new JPanel(new GridLayout());
		((GridLayout)genrePanel.getLayout()).setHgap(5);
		genrePanel.add(genresComboBox);
		genrePanel.add(genreField);
		formPanel.add(genrePanel, getRightGBC(1, 3));

		// Publishers
		addLabelToPanel("Publishers", formPanel, 0, 4);
		publishersComboBox = new JComboBox<ComboBoxItem>(getComboBox("publisher", true));

		JPanel publisherPanel = new JPanel(new GridLayout());
		((GridLayout)publisherPanel.getLayout()).setHgap(5);
		publisherPanel.add(publishersComboBox);
		publisherPanel.add(publisherField);
		formPanel.add(publisherPanel, getRightGBC(1,4));

		// Buttons
		JPanel buttonsPanel = new JPanel();
		JButton clearButton = new JButton("Clear Form");
		clearButton.addActionListener(new ClearAction());
		buttonsPanel.add(clearButton);
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new SearchAction());
		buttonsPanel.add(searchButton);

		// Table
		setupPanel(formPanel, buttonsPanel);
	}
	
	private void clearForm() {
		titleField.setText("");
		descriptionField.setText("");
		releaseDateChooser.setDate(null);
		publisherField.setText("");
		genreField.setText("");
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
	        
	        if(!publisherField.getText().isBlank()) {
	        	query += " AND p.name LIKE ?";
	        	parameters.add("%" + publisherField.getText().toUpperCase() + "%");
	        }
	        
	        int genreId = ((ComboBoxItem) genresComboBox.getSelectedItem()).getId();
	        if(genreId != 0) {
	            query += " AND g.genre_id = ?";
	            parameters.add(genreId);
	        }
	        
	        if(!genreField.getText().isBlank()) {
	        	query += " AND gr.name LIKE ?";
	        	parameters.add("%" + genreField.getText().toUpperCase() + "%");
	        }

	        PreparedStatement statement = connection.prepareStatement(query);
	        
	        // Bind parameters
	        int parameterIndex = 1;
	        for (Object parameter : parameters) {
	            if (parameter instanceof Date) {
	                statement.setDate(parameterIndex++, (Date) parameter);
	            } else if(parameter instanceof Integer) {
	            	statement.setInt(parameterIndex, (int)parameter);
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
