package net.guides.view;

import java.util.List;

public interface Loader<T> {
    List<T> load();
}
