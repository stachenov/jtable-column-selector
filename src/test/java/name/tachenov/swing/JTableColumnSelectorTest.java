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

    private static final String DP_COLUMN_INDEXES = "columnIndexes";
    private static final int A_REASONABLE_COLUMN_COUNT = 3;
    private JTable table;
    private JTableColumnSelector tcs;
    private List<JCheckBoxMenuItem> menuItems;
    private TableColumnModel columnModel;
    
    @AfterMethod
    public void cleanUp() {
        table = null; tcs = null; menuItems = null; columnModel = null;
    }
    
    @Test
    public void create() {
        tcs = new JTableColumnSelector();
        assertThat(tcs).isNotNull(); // Not necessary, but makes FindBugs happy.
    }
    
    @Test
    public void install() {
        tcs = new JTableColumnSelector();
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        table = createTable(columnCount);
        tcs.install(table);
        JPopupMenu headerMenu = table.getTableHeader().getComponentPopupMenu();
        assertThat(headerMenu).isNotNull();
        assertThat(headerMenu.getComponentCount()).isEqualTo(columnCount);
        List<String> columnNames = getModelColumnNames(table);
        menuItems = getMenuItems(table);
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
        tcs = new JTableColumnSelector();
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        table = createTable(columnCount);
        final TableColumn firstColumn = table.getColumnModel().getColumn(0);
        table.getColumnModel().removeColumn(firstColumn);
        tcs.install(table);
        JPopupMenu headerMenu = table.getTableHeader().getComponentPopupMenu();
        assertThat(headerMenu.getComponentCount()).isEqualTo(columnCount);
    }
    
    @Test
    public void installsProperlyWhenTableHasDefaultEmptyModel() {
        tcs = new JTableColumnSelector();
        table = new JTable();
        tcs.install(table);
    }
    
    @DataProvider(name = DP_COLUMN_INDEXES)
    public Object[][] columnIndexes() {
        return new Object[][] {{0}, {1}};
    }
    
    @Test(dataProvider = DP_COLUMN_INDEXES)
    public void menuItemsHideColumns(int columnIndex) {
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        table = createTable(columnCount);
        tcs = new JTableColumnSelector();
        tcs.install(table);
        menuItems = getMenuItems(table);
        String columnName = table.getColumnName(columnIndex);
        menuItems.get(columnIndex).doClick();
        assertThat(table.getColumnCount()).isEqualTo(columnCount - 1);
        assertThat(getViewColumnNames(table)).doesNotContain(columnName);
    }

    private static List<String> getViewColumnNames(JTable table) {
        return IntStream.range(0, table.getColumnCount())
                .mapToObj(i -> table.getColumnName(i))
                .collect(Collectors.toList());
    }

    private void setUpTableInstallTCSAndGetTheComponents(int columnCount) {
        table = createTable(columnCount);
        tcs = new JTableColumnSelector();
        tcs.install(table);
        menuItems = getMenuItems(table);
        columnModel = table.getColumnModel();
    }
    
    @Test
    public void theRightColumnIsHiddenEvenIfColumnsAreRearranged() {
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        setUpTableInstallTCSAndGetTheComponents(columnCount);
        String firstColumnName = table.getColumnName(0);
        columnModel.moveColumn(0, columnCount - 1);
        menuItems.get(0).doClick();
        assertThat(getViewColumnNames(table)).doesNotContain(firstColumnName);
    }
    
    @Test
    public void canShowHiddenColumn() {
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        setUpTableInstallTCSAndGetTheComponents(columnCount);
        menuItems.get(0).doClick();
        menuItems.get(0).doClick();
        assertThat(table.getColumnCount()).isEqualTo(columnCount);
    }
    
    @Test
    public void shownColumnReappearsInItsPlaceWhenUsingModelOrder() {
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        setUpTableInstallTCSAndGetTheComponents(columnCount);
        String firstColumnName = table.getColumnName(0);
        menuItems.get(0).doClick();
        menuItems.get(0).doClick();
        assertThat(table.getColumnName(0)).isEqualTo(firstColumnName);
    }
    
    @Test
    public void columnReappearsSafelyEvenIfOtherColumnsWereRemoved() {
        final int columnCount = A_REASONABLE_COLUMN_COUNT;
        setUpTableInstallTCSAndGetTheComponents(columnCount);
        int lastIndex = columnCount - 1;
        String lastColumnName = table.getColumnName(lastIndex);
        menuItems.get(lastIndex).doClick();
        menuItems.get(0).doClick();
        menuItems.get(lastIndex).doClick();
        lastIndex = table.getColumnCount() - 1;
        assertThat(table.getColumnName(lastIndex)).isEqualTo(lastColumnName);
    }

    private static class ColumnNameAnswer implements Answer<String> {

        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            return "column" + invocation.getArgument(0);
        }
    }

}
