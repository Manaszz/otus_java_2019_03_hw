package ru.otus.kchu;

public class ThreadHW {
    private int state = 0;
    private final Object monitor = new Object();

    public static void main(String[] args) throws InterruptedException {
        ThreadHW counter = new ThreadHW();
        counter.go();
    }

    private void go() {
        Thread thread1 = new Thread(() -> {
            int j = 1;
            for (int i = 1; i < 20; i++) {
                inc1((i < 10) ? j++ : j--);
            }
        });

        Thread thread2 = new Thread(() -> {
            int j = 1;
            for (int i = 1; i < 20; i++) {
                inc2((i < 10) ? j++ : j--);
            }
        }
        );

        thread1.start();
        thread2.start();

    }

    private void inc1(int i) {
        synchronized (monitor) {
            while (state == 1) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print("(T1)" + ":" + i + ";  ");
            state = 1;
            monitor.notify();
        }
    }

    private void inc2(int i) {
        synchronized (monitor) {
            while (state == 0) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("(T2)" + ":" + i + ";  ");
            state = 0;
            monitor.notify();
        }
    }
}
