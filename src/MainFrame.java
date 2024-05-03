import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {
	private JTabbedPane tab = new JTabbedPane();

	private JPanel gamesPanel = new JPanel();
	private JPanel publishersPanel = new JPanel();
	private JPanel genresPanel = new JPanel();
	private JPanel countriesPanel = new JPanel();

	private JTable gamesTable = new JTable();
	JScrollPane gamesTableScroll = new JScrollPane(gamesTable);
	private JTable publishersTable = new JTable();
	JScrollPane publishersTableScroll = new JScrollPane(publishersTable);
	private JTable genresTable = new JTable();
	JScrollPane genresTableScroll = new JScrollPane(genresTable);
	private JTable countriesTable = new JTable();
	JScrollPane countriesTableScroll = new JScrollPane(countriesTable);
	
	public MainFrame() {
		this.setTitle("Game Store");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		setupGamesPanel();
		setupPublishersPanel();
		setupGenresPanel();
		setupCountriesPanel();

		tab.addChangeListener(getTabChangeListener());

		this.add(tab);
		
		loadGames();
	}
	
	private ChangeListener getTabChangeListener() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				switch (index) {
				case 0:
					loadGames();
					break;
				case 1:
					loadPublishers();
					break;
				case 2:
					loadGenres();
					break;
				case 3:
					loadCountries();
					break;
				}
			}
		};
	}
	
	private void setupGamesPanel() {
		gamesPanel.setLayout(new GridBagLayout());
		
		JLabel gamesLabel = new JLabel("Games");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gamesPanel.add(gamesLabel, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gamesPanel.add(gamesTableScroll, gbc);
		
		tab.add(gamesPanel, "Games");
	}
	
	private void setupPublishersPanel() {
		publishersPanel.setLayout(new GridBagLayout());
		
		JLabel publishersLabel = new JLabel("Publishers");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		publishersPanel.add(publishersLabel, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		publishersPanel.add(publishersTableScroll, gbc);
		
		tab.add(publishersPanel, "Publishers");
	}
	
	private void setupGenresPanel() {
		genresPanel.setLayout(new GridBagLayout());
		
		JLabel genresLabel = new JLabel("Genres");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		genresPanel.add(genresLabel, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		genresPanel.add(genresTableScroll, gbc);
		
		tab.add(genresPanel, "Genres");
	}
	
	private void setupCountriesPanel() {
		countriesPanel.setLayout(new GridBagLayout());
		
		JLabel countriesLabel = new JLabel("Countries");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		countriesPanel.add(countriesLabel, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		countriesPanel.add(countriesTableScroll, gbc);
		
		tab.add(countriesPanel, "Countries");
	}

	private void loadGames() {
		Connection connection = DbConnection.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT title, description, release_date as `Release Date` FROM game");

			ResultSet result = statement.executeQuery();

			gamesTable.setModel(new GameStoreTableModel(result));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadPublishers() {
		Connection connection = DbConnection.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM publisher");
			
			ResultSet result = statement.executeQuery();
			
			publishersTable.setModel(new GameStoreTableModel(result));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadGenres() {
		Connection connection = DbConnection.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM genre");
			
			ResultSet result = statement.executeQuery();
			
			genresTable.setModel(new GameStoreTableModel(result));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadCountries() {
		Connection connection = DbConnection.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM country");
			
			ResultSet result = statement.executeQuery();
			
			countriesTable.setModel(new GameStoreTableModel(result));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
