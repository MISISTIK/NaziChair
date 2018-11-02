package itea.homework;

import java.util.concurrent.TimeUnit;

import static itea.homework.Main.*;

public class Chair {
    void getChair(String name) {
        try {
            semaphore.acquire();
            print("Deutschen Soldaten " +name + " SIT ON CHAIR", CmdColor.ANSI_CYAN);
            for (int i = 0; i < sitSec; i++) {
                try {
                    print("Sitting " + String.valueOf(i+1) + " second" + (i==0 ? "" : "s") + " ...", CmdColor.ANSI_BLUE);
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
