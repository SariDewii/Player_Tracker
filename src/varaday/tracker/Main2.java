package varaday.tracker;

import varaday.tracker.actionbars.ActionBarEvent;
import org.bukkit.Bukkit;

public class Main2 {
    public Main2() {
    }

    public static void everyFiveTicks() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {
                Main.checkHand();
                Main.refreshTracker();
            }
        }, 0L, 5L);
    }

    public static void everySecond() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {
                ActionBarEvent.clearAll();
            }
        }, 0L, 20L);
    }
}

