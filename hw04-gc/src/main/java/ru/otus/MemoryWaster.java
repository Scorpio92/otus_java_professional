package ru.otus;

import java.util.ArrayList;
import java.util.List;

public class MemoryWaster implements MemoryWasterMBean {

    private List<byte[]> list = new ArrayList<>();

    private int loopCount;
    private int size;
    private int pauseDuration;
    private int removePercent;

    void run() throws InterruptedException {
        for (int idx = 0; idx < loopCount; idx++) {
            for (int i = 0; i < size; i++) {
                list.add(new byte[1024]);
            }
            Thread.sleep(pauseDuration);
            for (int i = 0; i < size * removePercent/100; i++) {
                list.remove(i);
            }
            System.out.println("iterations: " + (idx + 1));
        }
    }

    @Override
    public void setLoopCount(int count) {
        this.loopCount = count;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setPauseDuration(int pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    @Override
    public void setRemovePercent(int removePercent) {
        this.removePercent = removePercent;
    }
}
