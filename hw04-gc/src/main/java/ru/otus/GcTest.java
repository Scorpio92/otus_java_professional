package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//-XX:+UseSerialGC
//-XX:+UseConcMarkSweepGC
//-XX:+UseG1GC
public class GcTest {

    public static void main(String... args) throws Exception {
        Stat stat = new Stat();
        switchOnMonitoring(stat);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=basic");

        MemoryWaster mBean = new MemoryWaster();
        mBean.setLoopCount(100000);
        mBean.setSize(192);
        mBean.setPauseDuration(10);
        mBean.setRemovePercent(50);
        mbs.registerMBean(mBean, name);
        mBean.run();
    }

    private static void switchOnMonitoring(Stat stat) {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                    stat.register(gcName + gcAction, (int) duration);
                    stat.conclusion();
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    public static class Stat {

        private final long beginTime = System.currentTimeMillis();
        private final AtomicInteger youngCounter = new AtomicInteger();
        private final AtomicInteger oldCounter = new AtomicInteger();
        private final AtomicInteger youngMaxDuration = new AtomicInteger();
        private final AtomicInteger oldMaxDuration = new AtomicInteger();

        public void register(String event, int duration) {
            String s = event.toLowerCase();
            if (s.contains("minor")) { ;
                if (youngMaxDuration.get() < duration) {
                    youngMaxDuration.set(duration);
                }
                System.out.printf("Young event [%s], duration [%s] ms\n", youngCounter.incrementAndGet(), duration);
            }
            if (s.contains("major")) { ;
                if (oldMaxDuration.get() < duration) {
                    oldMaxDuration.set(duration);
                }
                System.out.printf("Old event [%s], duration [%s] ms\n", oldCounter.incrementAndGet(), duration);
            }
        }

        public void conclusion() {
            long endTimeMs = (System.currentTimeMillis() - beginTime);
            System.out.println("\n");
            if (youngCounter.get() > 0) {
                System.out.printf("Young counts: %s, max duration: %s ms\n", youngCounter.get(), youngMaxDuration.get());
            }
            if (oldCounter.get() > 0) {
                System.out.printf("Old counts: %s, max duration: %s ms\n", oldCounter.get(), oldMaxDuration.get());
            }
            System.out.printf("Work time [%s] sec\n", endTimeMs / 1000);
        }
    }
}
