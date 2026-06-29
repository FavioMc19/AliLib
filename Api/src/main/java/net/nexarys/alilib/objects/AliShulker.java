package net.nexarys.alilib.objects;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface AliShulker {
    void update(Location location);
    void remove();
    void setGlowing(boolean glowing);
    void setScale(float scale);
    void update();
    void mount(Entity target);
    void setPeek(float peek);
    Location getLocation();
}
