/*
 * (C) Sergey A. Tachenov
 * This thing belongs to public domain. Really.
 */
package name.tachenov.swing;

import java.util.*;
import java.util.stream.*;
import javax.swing.*;
import javax.swing.table.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.invocation.*;
import org.mockito.stubbing.*;
import org.testng.annotations.*;

public class JTableColumnSelectorTest {

    private static final int A_REASONABLE_COLUMN_COUNT = 3;
    
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
        List<String> columnNames = getModelColumnNames(table);
        List<JCheckBoxMenuItem> menuItems = getMenuItems(table);
        assertThat(menuItems)
                .extracting(item -> item.getText())
                .containsExactlyElementsOf(columnNames);
        assertThat(menuItems)
                .extracting(item -> item.isSelected())
                .containsOnly(true);
    }
    
    private JTable createTable(final int columnCount) {
        TableModel model = mock(TableModel.class);
        when(model.getColumnCount()).thenReturn(columnCount);
        when(model.getColumnName(anyInt())).thenAnswer(new ColumnNameAnswer());
        return new JTable(model);
    }
    
    private static List<String> getModelColumnNames(JTable table) {
        final TableModel model = table.getModel();
        return IntStream.range(0, model.getColumnCount())
                .mapToObj(i -> model.getColumnName(i))
                .collect(Collectors.toList());
    }

    private static List<JCheckBoxMenuItem> getMenuItems(JTable table) {
        JPopupMenu menu = table.getTableHeader().getComponentPopupMenu();
        return IntStream.range(0, menu.getComponentCount())
                .mapToObj(i -> (JCheckBoxMenuItem) menu.getComponent(i))
                .collect(Collectors.toList());
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
    
    @Test
    public void installsProperlyWhenTableHasDefaultEmptyModel() {
        JTableColumnSelector tcs = new JTableColumnSelector();
        JTable table = new JTable();
        tcs.install(table);
    }
    
    @Test
    public void menuItemsHideColumns() {
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        JTable table = createTable(columnCount);
        JTableColumnSelector tcs = new JTableColumnSelector();
        tcs.install(table);
        List<JCheckBoxMenuItem> menuItems = getMenuItems(table);
        menuItems.get(0).doClick();
        assertThat(table.getColumnCount()).isEqualTo(columnCount - 1);
    }

    private static class ColumnNameAnswer implements Answer<String> {

        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            return "column" + invocation.getArgument(0);
        }
    }

}
