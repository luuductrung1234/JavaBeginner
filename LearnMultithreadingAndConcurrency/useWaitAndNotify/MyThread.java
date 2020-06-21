package useWaitAndNotify;

public class MyThread implements Runnable {
    Thread thread;
    TickTock tickTock;

    public MyThread(String name, TickTock tickTock) {
        this.thread = new Thread(this, name);
        this.tickTock = tickTock;
    }

    public static MyThread createAndStart(String name, TickTock tickTock) {
        MyThread myThread = new MyThread(name, tickTock);
        myThread.thread.start();
        return myThread;
    }

    @Override
    public void run() {
        if (thread.getName().compareTo("Tick") == 0) {
            for (int i = 0; i < 5; i++) {
                tickTock.tick(true);
            }
            tickTock.tick(false);
        } else {
            for (int i = 0; i < 5; i++) {
                tickTock.tock(true);
            }
            tickTock.tock(false);
        }
    }
}