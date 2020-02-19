package net.guides.view;

import net.guides.data.DataAccessFacade;
import net.guides.view.loader.ClientLoader;
import net.guides.view.loader.EventLoader;
import net.guides.view.loader.PaymentLoader;
import net.guides.view.loader.PaymentTypeLoader;
import net.guides.view.tab.TabImpl;
import net.guides.view.table.ClientColumnMapper;
import net.guides.view.table.EventColumnMapper;
import net.guides.view.table.PaymentColumnMapper;
import net.guides.view.table.PaymentTypeColumnMapper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TabbedListTableView {
    private JTabbedPane tabbedPane;

    public TabbedListTableView(DataAccessFacade dataAccessFacade) {
        tabbedPane = new JTabbedPane();
        java.util.List<Tab> tabs = new ArrayList<>();
        tabs.add(new TabImpl<>("Clients", new ClientLoader(dataAccessFacade), new ClientColumnMapper()));
        tabs.add(new TabImpl<>("Events", new EventLoader(dataAccessFacade), new EventColumnMapper()));
        tabs.add(new TabImpl<>("Payment Types", new PaymentTypeLoader(dataAccessFacade), new PaymentTypeColumnMapper()));
        tabs.add(new TabImpl<>("Payments", new PaymentLoader(dataAccessFacade), new PaymentColumnMapper()));

        for (Tab tab : tabs) {
            tabbedPane.addTab(tab.getTabName(), tab.getContainer());
        }
    }

    public void addToContainer(Container container, Object constraints) {
        container.add(tabbedPane, constraints);
    }
}
