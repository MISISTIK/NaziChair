package itea.homework;

import java.util.concurrent.TimeUnit;

import static itea.homework.Main.BARRIER;
import static itea.homework.Main.print;
import static itea.homework.Main.semaphore;
import static itea.homework.Main.player;

public class DeutschenSoldaten implements Runnable {

    private Thread th;
    private String name;
    private Chair chair;

    public DeutschenSoldaten(String name, Chair chair) {
        this.th = new Thread(this);
        this.name = name;
        this.chair = chair;
        this.th.start();
        this.th.setName("Nazi-" + name);

    }

    public Thread getThread() {
        return th;
    }

    @Override
    public void run() {
         {
            try {
                //Uncomment this to visualize the thread statuses
                //new spammingThread(this.getThread());
                System.out.println("Deutschen Soldaten " + name + " awaits of game start");
                BARRIER.await();
                chair.getChair(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            print("Deutschen Soldaten " +name + " LEAVES THE GAME", CmdColor.ANSI_RED);
            player.play();
        }
    }

    private class spammingThread implements Runnable {

        private Thread parentThread;
        private Thread thisThread;

        public spammingThread(Thread parentThread) {
            this.parentThread = parentThread;
            this.thisThread = new Thread(this);
            this.thisThread.setDaemon(true);
            this.thisThread.start();
        }

        @Override
        public void run() {
            while (parentThread.isAlive()) {
                System.out.println("Status: Deutschen Soldaten " + name + " = " + parentThread.getState().toString());
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
