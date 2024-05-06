package Utils;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class ComboBoxRenderer extends DefaultListCellRenderer {
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof ComboBoxItem) {
        	ComboBoxItem country = (ComboBoxItem) value;
            setText(country.getName());
        }
        return this;
    }
}
