package com.pld.agile.view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Display the cells in the tree depending on their values.
 */
public class DeliveriesTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
        String node = (String) ((DefaultMutableTreeNode) value).getUserObject();
        if (leaf && node.endsWith("(Out of Time Window!)")) {
            setForeground(Color.RED);
        }
        if (leaf && node.startsWith("Wait")) {
            setForeground(Color.BLUE);
        }
        return this;
    }
}
