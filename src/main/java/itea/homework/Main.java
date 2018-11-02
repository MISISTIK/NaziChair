package itea.homework;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {

    volatile static MediaPlayer player;
    private static int THREAD_COUNT = 5;
    static CountDownLatch BARRIER = new CountDownLatch(1);
    static Semaphore semaphore = new Semaphore(1);
    static int sitSec = 3;
    static boolean isFirstTimePlayed = true;


    public static void main(String[] args) {
        try {
            final JFXPanel fxPanel = new JFXPanel();
            Chair chair = new Chair();
            Thread[] pool = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                pool[i] = new DeutschenSoldaten(String.valueOf(i + 1), chair).getThread();
            }

            mediaPlayerInit();

            for (int i = 0; i < THREAD_COUNT; i++) {
                try {
                    pool[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            player.stop();
            print("Player stopped.",CmdColor.ANSI_GREEN);
            print("===== GAME ENDS!=====", CmdColor.ANSI_PURPLE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }

    }

    static void print(String s, CmdColor color) {
        System.out.println(color.getColor() + s + CmdColor.ANSI_RESET.getColor());
    }

    static void mediaPlayerInit() {
        String musicPath = Main.class.getResource("/DROELOE-In Time.m4a").toString();
        Media hit = new Media(musicPath);
        player = new MediaPlayer(hit);
        player.setStartTime(Duration.ZERO);
        player.setOnEndOfMedia(() -> {player.seek(Duration.ZERO); player.play();});
        player.setOnPaused(() -> {
            print("Player paused. Run nazi, run!!!",CmdColor.ANSI_GREEN);
            if (!isFirstTimePlayed) {
                semaphore.release();
            } else {
                BARRIER.countDown();
                isFirstTimePlayed = false;
            }
        });
        player.setOnPlaying(() -> {
            try {
                long v = 3000 + ((long) ((Math.random() + 1) * 1000) * 2);
                print("Player playing for " +  String.valueOf(v) + " ms",CmdColor.ANSI_GREEN);
                TimeUnit.MILLISECONDS.sleep(v);
                player.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        print("===== GAME STARTED!=====", CmdColor.ANSI_PURPLE);
        player.play();
    }
}
