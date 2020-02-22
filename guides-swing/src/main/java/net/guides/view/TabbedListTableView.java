package net.guides.view;

import java.awt.Container;
import javax.swing.JTabbedPane;
import java.util.List;

public class TabbedListTableView {
    private JTabbedPane tabbedPane;

    public TabbedListTableView(List<Tab> tabs) {
        tabbedPane = new JTabbedPane();

        for (Tab tab : tabs) {
            tabbedPane.addTab(tab.getTabName(), tab.getContainer());
        }
    }

    public void addToContainer(Container container, Object constraints) {
        container.add(tabbedPane, constraints);
    }
}
