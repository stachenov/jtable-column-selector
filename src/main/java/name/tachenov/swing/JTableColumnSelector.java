/*
 * (C) Sergey A. Tachenov
 * This thing belongs to public domain. Really.
 */
package name.tachenov.swing;

import javax.swing.*;
import javax.swing.table.*;

/**
 * A class that allows user to select visible columns of a JTable using a popup menu.
 * 
 * @author Sergey A. Tachenov
 */
class JTableColumnSelector {

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
            hideColumn(modelIndex);
        });
        return menuItem;
    }

    private void hideColumn(int modelIndex) {
        int vIndex = table.convertColumnIndexToView(modelIndex);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.removeColumn(columnModel.getColumn(vIndex));
    }
    
}
