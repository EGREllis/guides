package net.guides.view;

import java.awt.*;

public interface Tab extends Listener {
    Container getContainer();
    String getTabName();
}
