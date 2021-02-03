package varaday.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import varaday.tracker.actionbars.ActionBar;
import varaday.tracker.actionbars.ActionBarEvent;
import varaday.tracker.menus.GUIMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main plugin;
    public static HashMap<Player, Player> tracks = new HashMap();
    public static String prefix;
    public static boolean recipe;
    public static String format;
    public static String item_name;
    public static List<String> item_lore = new ArrayList();
    public static boolean gui;
    public static boolean defCompass;

    public Main() {
    }

    public static Main getInstance() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        Track2.everyFiveTicks();
        Track2.everySecond();
        this.getServer().getPluginManager().registerEvents(new GUIMenu(), this);
        this.getCommand("track").setExecutor(new TrackCMD());
        this.saveDefaultConfig();
        this.reloadConfigFile();
    }

    public void reloadConfigFile() {
        String previous_name = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("track.name"));
        if (item_name != null) {
            previous_name = item_name;
        }

        this.reloadConfig();
        prefix = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix") + " ");
        recipe = this.getConfig().getBoolean("customrecipe");
        gui = this.getConfig().getBoolean("gui");
        defCompass = this.getConfig().getBoolean("defaultcompass");
        format = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("format"));
        item_name = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("track.name"));
        item_lore.clear();
        Iterator var2 = this.getConfig().getStringList("track.lore").iterator();

        while(var2.hasNext()) {
            String line = (String)var2.next();
            item_lore.remove(ChatColor.translateAlternateColorCodes('&', line));
        }

        ItemStack item = getTracker();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(previous_name);
        item.setItemMeta(meta);
        Iterator iterator = this.getServer().recipeIterator();

        while(iterator.hasNext()) {
            if (((Recipe)iterator.next()).getResult().isSimilar(item)) {
                iterator.remove();
            }
        }

        ShapelessRecipe rec = new ShapelessRecipe(getTracker());
        rec.addIngredient(1, Material.COMPASS);
        if (recipe) {
            this.getServer().addRecipe(rec);
        }
    }

    public static ItemStack getTracker() {
        ItemStack tracker = new ItemStack(Material.COMPASS);
        ItemMeta tracker_meta = tracker.getItemMeta();
        tracker_meta.setDisplayName(item_name);
        tracker_meta.setLore(item_lore);
        tracker.setItemMeta(tracker_meta);
        return tracker;
    }

    public static void refreshTracker() {
        Iterator var0 = Bukkit.getOnlinePlayers().iterator();

        while(var0.hasNext()) {
            Player p = (Player)var0.next();
            World world = p.getWorld();
            Player nearest = null;
            double nearest_range = 0.0D;
            Iterator var6 = Bukkit.getOnlinePlayers().iterator();

            while(var6.hasNext()) {
                Player target = (Player)var6.next();
                if (target.getWorld().equals(world) && !target.equals(p)) {
                    double x = Math.abs(p.getLocation().getX() - target.getLocation().getX());
                    double y = Math.abs(p.getLocation().getY() - target.getLocation().getY());
                    double z = Math.abs(p.getLocation().getZ() - target.getLocation().getZ());
                    double range = Math.sqrt(Math.pow(x, 2.0D) + Math.pow(y, 2.0D) + Math.pow(z, 2.0D));

                    if (nearest == null) {
                        nearest = target;
                        nearest_range = range;
                    }

                    if (range < nearest_range) {
                        nearest = target;
                        nearest_range = range;
                    }
                }
            }

            if (nearest == null) {
                tracks.remove(p);
                p.setCompassTarget(p.getLocation());
                return;
            }

            tracks.put(p, nearest);
        }

    }

    public static String getFormatted(Player p, Player target, double range) {
        String new_format = format;
        new_format = new_format.replace("{name}", p.getName());
        new_format = new_format.replace("{target}", target.getName());
        return new_format.replace("{range}", String.valueOf((int)range));
    }

    public static void setBar(final Player p, Player target, double range) {
        ActionBarEvent.using.put(p, true);
        ActionBar bar = new ActionBar();
        bar.setMessage(getFormatted(p, target, range)).send(p);
        Bukkit.getScheduler().runTaskLater(getInstance(), new Runnable() {

            public void run() {
                ItemStack tracker = Main.getTracker();
                tracker.setAmount(p.getItemInHand().getAmount());
                if (!p.getItemInHand().equals(tracker)) {
                    ActionBarEvent.using.put(p, false);
                }

            }
        }, 60L);
    }

    public static void checkHand() {
        Iterator var0 = Bukkit.getOnlinePlayers().iterator();

        while(true) {
            while(var0.hasNext()) {
                Player p = (Player)var0.next();
                if (!tracks.containsKey(p)) {
                    return;
                }

                ItemStack tracker = getTracker();
                tracker.setAmount(p.getItemInHand().getAmount());
                if (!p.getItemInHand().equals(tracker) && (!p.getItemInHand().getType().equals(Material.COMPASS) || !defCompass)) {
                    p.setCompassTarget(p.getLocation());
                } else {
                    Player target = (Player)tracks.get(p);
                    p.setCompassTarget(target.getLocation());
                    setBar(p, target, getRange(p.getLocation(), target.getLocation()));
                }
            }

            return;
        }
    }

    public static double getRange(Location loc1, Location loc2) {
        double x = Math.abs(loc1.getX() - loc2.getX());
        double y = Math.abs(loc1.getY() - loc2.getY());
        double z = Math.abs(loc1.getZ() - loc2.getZ());
        return Math.sqrt(Math.pow(x, 2.0D) + Math.pow(y, 2.0D) + Math.pow(z, 2.0D));
    }
}

