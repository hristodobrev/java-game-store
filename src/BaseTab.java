import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public abstract class BaseTab {
	protected JPanel panel = new JPanel();
	protected JTable table = new JTable();
	protected JScrollPane tableScroll = new JScrollPane(table);
	protected String tableName;
	
	protected BaseTab(String tableName) {
		this.tableName = tableName;
	}

	public JPanel getPanel(){
		return panel;
	}
	
	public void loadData() {
		Connection connection = DbConnection.getConnection();
		try {
			String query = "SELECT * FROM " + this.tableName;
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();

			table.setModel(new GameStoreTableModel(result));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
