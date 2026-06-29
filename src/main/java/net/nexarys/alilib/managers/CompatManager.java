package net.nexarys.alilib.managers;

import net.nexarys.alilib.objects.Compat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class CompatManager {

    private static CompatManager instance;

    public static CompatManager init(JavaPlugin plugin) {
        if (instance == null) {
            instance = new CompatManager(plugin);
        }
        return instance;
    }

    public static CompatManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CompatManager has not been initialized. Call CompatManager.init(plugin) first.");
        }
        return instance;
    }

    private String VERSION = "";

    private final JavaPlugin plugin;
    private final Compat compat;

    private final Map<String, String> versions = new HashMap<>() {{
        put("1.20.6", "v1_20_R2");
        put("1.21", "v1_21_R1");
        put("1.21.1", "v1_21_R1");
        put("1.21.2", "v1_21_R1");
        put("1.21.3", "v1_21_R2");
        put("1.21.4", "v1_21_R3");
        put("1.21.5", "v1_21_R4");
        put("1.21.6", "v1_21_R5");
        put("1.21.7", "v1_21_R5");
        put("1.21.8", "v1_21_R5");
        put("1.21.9", "v1_21_R6");
        put("1.21.10", "v1_21_R6");
        put("1.21.11", "v1_21_R7");
        put("26.1", "v26_1");
        put("26.1.1", "v26_1");
        put("26.1.2", "v26_1");
        put("26.2", "v26_2");
    }};

    private CompatManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setVersionCompat();
        this.compat = loadCompat();
    }

    private void setVersionCompat() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();

        if (packageName.contains("v")) {
            VERSION = packageName.substring(packageName.lastIndexOf('.') + 1);
        } else {
            String bukkitVersion = Bukkit.getBukkitVersion().split("-")[0];
            int buildIndex = bukkitVersion.indexOf(".build.");

            if (buildIndex != -1) {
                bukkitVersion = bukkitVersion.substring(0, buildIndex);
            }

            VERSION = versions.getOrDefault(bukkitVersion, "[Version not found. %s]".formatted(bukkitVersion));
        }
    }

    private Compat loadCompat() {
        Compat compat = tryCompat(VERSION);

        if (compat == null) {
            plugin.getLogger().severe("Version not supported: [" + VERSION + "]");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return null;
        }

        plugin.getLogger().info("AliLib loaded! Selected version: [" + VERSION + "]");
        return compat;
    }

    private Compat tryCompat(String version) {
        try {
            Class<?> compatClass = Class.forName("net.nexarys.alilib.version." + version);

            if (Compat.class.isAssignableFrom(compatClass)) {
                return (Compat) compatClass.getDeclaredConstructor().newInstance();
            }

        } catch (Exception ignored) {
        }

        return null;
    }

    public Compat getCompat() {
        return compat;
    }

    public String getVersion() {
        return VERSION;
    }
}