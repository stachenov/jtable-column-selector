/*
 * (C) Sergey A. Tachenov
 * This thing belongs to public domain. Really.
 */
package name.tachenov.swing;

import javax.swing.*;
import static org.assertj.core.api.Assertions.*;
import org.testng.annotations.*;

public class JTableColumnSelectorTest {
    
    @Test
    public void create() {
        JTableColumnSelector tcs = new JTableColumnSelector();
        assertThat(tcs).isNotNull(); // Not necessary, but makes FindBugs happy.
    }
    
    @Test
    public void install() {
        JTableColumnSelector tcs = new JTableColumnSelector();
        JTable table = new JTable();
        tcs.install(table);
    }
    
}
