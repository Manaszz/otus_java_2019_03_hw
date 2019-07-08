package ru.otus.kchu.cache;

/**
 * Created by tully.
 */
@SuppressWarnings("WeakerAccess")
public class MyElement<K, T> {
    private final K key;
    private final T value;
    private final long creationTime;
    private long lastAccessTime;


    public MyElement(K key, T value) {
        this.key = key;
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public K getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }

    @Override
    public String toString() {
        return "MyElement{" +
                "key=" + key +
                ", value=" + value +
                ", creationTime=" + creationTime +
                ", lastAccessTime=" + lastAccessTime +
                '}';
    }
}
