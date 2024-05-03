import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class MainFrame extends JFrame {
	public MainFrame() {
		this.setSize(600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Game Store");
		
		JTabbedPane tab = new JTabbedPane();
		
		JPanel gamesPanel = new JPanel();
		JPanel publishersPanel = new JPanel();
		JPanel genresPanel = new JPanel();
		JPanel countriesPanel = new JPanel();
		
		JLabel gamesLabel = new JLabel("Games");
		JLabel publishersLabel = new JLabel("Publishers");
		JLabel genresLabel = new JLabel("Genres");
		JLabel countriesLabel = new JLabel("Countries");
		
		gamesPanel.add(gamesLabel);
		publishersPanel.add(publishersLabel);
		genresPanel.add(genresLabel);
		countriesPanel.add(countriesLabel);

		JTable gamesTable = new JTable();
		gamesPanel.add(gamesTable);
		
		tab.add(gamesPanel, "Games");
		tab.add(publishersPanel, "Publishers");
		tab.add(genresPanel, "Genres");
		tab.add(countriesPanel, "Countries");
		
		Connection connection = DbConnection.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM game");

			ResultSet result = statement.executeQuery();

			gamesTable.setModel(new GameStoreTableModel(result));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.add(tab);
		
		this.setVisible(true);
	}
}
