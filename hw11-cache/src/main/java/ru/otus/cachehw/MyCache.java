package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {

    private final WeakHashMap<K, V> map = new WeakHashMap<>();
    private final Collection<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        boolean isUpdate = map.containsKey(key);
        map.put(key, value);
        notifyObservers(key, value, isUpdate ? Action.UPDATE : Action.ADD);
    }

    @Override
    public void remove(K key) {
        V value = map.get(key);
        map.remove(key);
        notifyObservers(key, value, Action.REMOVE);
    }

    @Override
    public V get(K key) {
        V value = map.get(key);
        notifyObservers(key, value, Action.GET);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(hwListenerWeakReference -> {
            HwListener<K, V> listenerSaved = hwListenerWeakReference.get();
            return listenerSaved != null && listenerSaved.equals(listener);
        });
    }

    private void notifyObservers(K key, V value, Action action) {
        listeners.forEach(kvHwListener -> {
            HwListener<K, V> listener = kvHwListener.get();
            if (listener != null) listener.notify(key, value, action.name());
        });
    }
}
