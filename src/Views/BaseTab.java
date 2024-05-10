package Views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
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

	protected void setupPanel(JPanel formPanel, JPanel buttonsPanel) {
		GridBagConstraints gbc = new GridBagConstraints();
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
	
	protected void addLabelToPanel(String labelName, JPanel panel, int x, int y) {
		JLabel label = new JLabel(labelName);
		label.setMinimumSize(new Dimension(100, label.getMinimumSize().height));
		label.setPreferredSize(new Dimension(100, label.getPreferredSize().height));

		panel.add(label, getLeftGBC(x, y));
	}
	
	protected GridBagConstraints getLeftGBC(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 0, 0, 0);
		
		return gbc;
	}
	
	protected GridBagConstraints getRightGBC(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.weightx = 1.0;
		
		return gbc;
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
