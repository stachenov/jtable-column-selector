/*
 * (C) Sergey A. Tachenov
 * This thing belongs to public domain. Really.
 */
package name.tachenov.swing;

import javax.swing.*;
import javax.swing.table.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.testng.annotations.*;

public class JTableColumnSelectorTest {

    private static final int A_REASONABLE_COLUMN_COUNT = 10;
    
    @Test
    public void create() {
        JTableColumnSelector tcs = new JTableColumnSelector();
        assertThat(tcs).isNotNull(); // Not necessary, but makes FindBugs happy.
    }
    
    @Test
    public void install() {
        JTableColumnSelector tcs = new JTableColumnSelector();
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        JTable table = createTable(columnCount);
        tcs.install(table);
        JPopupMenu headerMenu = table.getTableHeader().getComponentPopupMenu();
        assertThat(headerMenu).isNotNull();
        assertThat(headerMenu.getComponentCount()).isEqualTo(columnCount);
    }
    
    @Test
    public void includesAllModelColumns() {
        JTableColumnSelector tcs = new JTableColumnSelector();
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        JTable table = createTable(columnCount);
        final TableColumn firstColumn = table.getColumnModel().getColumn(0);
        table.getColumnModel().removeColumn(firstColumn);
        tcs.install(table);
        JPopupMenu headerMenu = table.getTableHeader().getComponentPopupMenu();
        assertThat(headerMenu.getComponentCount()).isEqualTo(columnCount);
    }
    
    private JTable createTable(final int columnCount) {
        TableModel model = mock(TableModel.class);
        when(model.getColumnCount()).thenReturn(columnCount);
        return new JTable(model);
    }
    
}
