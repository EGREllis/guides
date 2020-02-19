package net.guides.view;

import net.guides.data.DataAccessFacade;

import java.awt.Container;
import javax.swing.JTabbedPane;
import java.util.List;

public class TabbedListTableView {
    private JTabbedPane tabbedPane;
    private List<Tab> tabs;

    public TabbedListTableView(DataAccessFacade dataAccessFacade, List<Tab> tabs) {
        tabbedPane = new JTabbedPane();

        for (Tab tab : tabs) {
            tabbedPane.addTab(tab.getTabName(), tab.getContainer());
        }
    }

    public void addToContainer(Container container, Object constraints) {
        container.add(tabbedPane, constraints);
    }
}
