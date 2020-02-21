package net.guides.view;

import net.guides.controller.Listener;

import java.awt.*;

public interface Tab extends Listener {
    Container getContainer();
    String getTabName();
}
