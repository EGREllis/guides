package net.guides.view.components;

import net.guides.view.Formatter;
import net.guides.view.Loader;

import javax.swing.*;
import java.util.List;

public class LoaderDrivenComboBox<T> {
    private final JComboBox<String> comboBox;
    private final Loader<T> loader;
    private final Formatter<T> formatter;
    private List<T> data;

    public LoaderDrivenComboBox(JComboBox<String> comboBox, Loader<T> loader, Formatter<T> formatter) {
        this.comboBox = comboBox;
        this.loader = loader;
        this.formatter = formatter;
        load();
    }

    public void load() {
        comboBox.removeAllItems();
        data = loader.load();
        for (T record : data) {
            String entry = formatter.format(record);
            comboBox.addItem(entry);
        }
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }


    public T getSelectedItem() {
        int index = comboBox.getSelectedIndex();
        T result = null;
        if (index >= 0) {
            result = data.get(index);
        }
        return result;
    }
}
