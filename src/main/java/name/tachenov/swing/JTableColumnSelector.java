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
        final JPopupMenu headerMenu = new JPopupMenu();
        TableModel model = table.getModel();
        for (int i = 0; i < model.getColumnCount(); ++i) {
            headerMenu.add(new JMenuItem());
        }
        table.getTableHeader().setComponentPopupMenu(headerMenu);
    }
    
}
