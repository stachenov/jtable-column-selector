/*
 * (C) Sergey A. Tachenov
 * This thing belongs to public domain. Really.
 */
package name.tachenov.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTableColumnSelectorDemo {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            JPanel root = new JPanel(new BorderLayout());
            TableModel model = new DefaultTableModel(new String[] {"A", "B", "C"}, 0);
            JTable table = new JTable(model);
            JTableColumnSelector tcs = new JTableColumnSelector();
            tcs.install(table);
            JScrollPane scrollPane = new JScrollPane(table);
            root.add(scrollPane, BorderLayout.CENTER);
            frame.setContentPane(root);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
    
}
