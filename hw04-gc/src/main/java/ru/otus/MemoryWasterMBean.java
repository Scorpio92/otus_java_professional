package ru.otus;

public interface MemoryWasterMBean {
    void setLoopCount(int count);
    void setSize(int size);
    void setPauseDuration(int pauseDuration);
    void setRemovePercent(int removePercent);
}
