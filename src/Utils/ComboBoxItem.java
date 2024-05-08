package Utils;

import javax.swing.JComboBox;

public class ComboBoxItem {
	private int id;
	private String name;

	public ComboBoxItem(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static void setSelectedId(int id, JComboBox<ComboBoxItem> comboBox) {
		for (int i = 0; i < comboBox.getItemCount(); i++) {
			ComboBoxItem item = comboBox.getItemAt(i);
			if (item.getId() == id) {
				comboBox.setSelectedIndex(i);
				break;
			}
		}
	}
}
