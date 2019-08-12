package ru.otus.kchu.services.cache;

/**
 * Created by tully.
 */
public interface CacheEngine<K, T> {

    void put(MyElement<K, T> element);

    MyElement<K, T> get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();
}
