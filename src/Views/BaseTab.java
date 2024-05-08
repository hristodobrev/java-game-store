package Views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Database.DbConnection;
import Utils.ComboBoxItem;
import Utils.GameStoreTableModel;

public abstract class BaseTab {
	protected JPanel panel = new JPanel();
	protected JTable table = new JTable();
	protected JScrollPane tableScroll = new JScrollPane(table);
	protected String tableName;
	protected String query = "SELECT * FROM ";

	protected BaseTab(String tableName) {
		this.tableName = tableName;
		this.query += tableName;
	}
	
	protected BaseTab(String tableName, String query) {
		this.tableName = tableName;
		this.query = query;
	}

	public JPanel getPanel() {
		return panel;
	}

	protected void loadData() {
		Connection connection = DbConnection.getConnection();
		try {
			String query = this.query;
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();

			table.setModel(new GameStoreTableModel(result));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected ComboBoxItem[] getComboBox(String tableName) {
		List<ComboBoxItem> genres = new ArrayList<ComboBoxItem>();

		try {
			Connection connection = DbConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM " + tableName);
			ResultSet resultSet = statement.executeQuery();

			genres.add(new ComboBoxItem(0, "All"));
			while (resultSet.next()) {
				genres.add(new ComboBoxItem(resultSet.getInt("id"), resultSet.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return genres.toArray(new ComboBoxItem[0]);
	}
}
