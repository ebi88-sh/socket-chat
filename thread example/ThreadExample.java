import java.util.*;
public class ThreadExample {

    private static int count = 0;

    // synchronized ensures safe increments
    private static synchronized void incrementByTen() {
        count += 10;
    }

    public static void main(String[] args) throws InterruptedException {

        // Using a synchronized HashSet (no arrays)
        Set<String> threadNames = Collections.synchronizedSet(new HashSet<>());

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                threadNames.add(Thread.currentThread().getName());
                incrementByTen();
            });
        }

        // start threads
        for (Thread t : threads) {
            t.start();
        }

        // wait for threads to finish
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Final Count = " + count);
        System.out.println("Threads used: " + threadNames);
    }
}
