/*
 * (C) Sergey A. Tachenov
 * This thing belongs to public domain. Really.
 */
package name.tachenov.swing;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * A class that allows user to select visible columns of a JTable using a popup menu.
 * 
 * @author Sergey A. Tachenov
 */
class JTableColumnSelector {

    private final Map<Integer, TableColumn> hiddenColumns = new HashMap<>();
    private JTable table;

    /**
     * Constructor. Call {@link #install(javax.swing.JTable) install} to actually
     * install it on a JTable.
     */
    public JTableColumnSelector() {
    }

    /**
     * Installs this selector on a given table.
     * @param table the table to install this selector on
     */
    public void install(JTable table) {
        this.table = table;
        table.getTableHeader().setComponentPopupMenu(createHeaderMenu());
    }

    private JPopupMenu createHeaderMenu() {
        final JPopupMenu headerMenu = new JPopupMenu();
        final TableModel model = table.getModel();
        for (int i = 0; i < model.getColumnCount(); ++i)
            headerMenu.add(createMenuItem(i));
        return headerMenu;
    }

    private JCheckBoxMenuItem createMenuItem(final int modelIndex) {
        final TableModel model = table.getModel();
        final String columnName = model.getColumnName(modelIndex);
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(columnName);
        menuItem.setSelected(true);
        menuItem.addActionListener(action -> {
            boolean show = menuItem.isSelected();
            if (show)
                showColumn(modelIndex);
            else
                hideColumn(modelIndex);
        });
        return menuItem;
    }

    private void showColumn(int modelIndex) {
        TableColumn column = hiddenColumns.remove(modelIndex);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.addColumn(column);
    }
    
    private void hideColumn(int modelIndex) {
        int vIndex = table.convertColumnIndexToView(modelIndex);
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column = columnModel.getColumn(vIndex);
        columnModel.removeColumn(column);
        hiddenColumns.put(modelIndex, column);
    }

}
