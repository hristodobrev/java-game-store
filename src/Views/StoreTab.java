package Views;

import java.awt.GridBagLayout;
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
	private JComboBox<ComboBoxItem> publishersComboBox;

	public StoreTab() {
		super("game", "SELECT g.id, g.title, g.description, g.release_date as `release date`, gr.id as `genre_id`, gr.name as `genre`, p.id as `publisher_id`, p.name as `publisher` FROM game g INNER JOIN genre gr ON gr.id = g.genre_id INNER JOIN publisher p ON p.id = g.publisher_id");

		panel.setLayout(new GridBagLayout());

		JPanel formPanel = new JPanel(new GridBagLayout());

		// Title
		addLabelToPanel("Title", formPanel, 0, 0);
		addTextFieldToPanel(titleField, formPanel, 1, 0);

		// Description
		addLabelToPanel("Description", formPanel, 0, 1);
		addTextFieldToPanel(descriptionField, formPanel, 1, 1);

		// Release Date
		addLabelToPanel("Release Date", formPanel, 0, 2);
		releaseDateChooser.setDateFormatString("yyyy-MM-dd");
		addDateFieldToPanel(releaseDateChooser, formPanel, 1, 2);

		// Genres
		addLabelToPanel("Genres", formPanel, 0, 3);
		genresComboBox = new JComboBox<ComboBoxItem>(getComboBox("genre"));
		addComboBoxToPanel(genresComboBox, formPanel, 1, 3);

		// Publishers
		addLabelToPanel("Publishers", formPanel, 0, 4);
		publishersComboBox = new JComboBox<ComboBoxItem>(getComboBox("publisher"));
		addComboBoxToPanel(publishersComboBox, formPanel, 1, 4);

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
