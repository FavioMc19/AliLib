package net.nexarys.alilib.objects;


import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Compat {
    AliTextDisplay createTextDisplay(List<Player> players, Location location, float yaw, float pitch);
    AliItemDisplay createItemDisplay(List<Player> players, Location location, float yaw, float pitch);
    AliBlockDisplay createBlockDisplay(List<Player> players, Location location, float yaw, float pitch);
    AliShulker createShulker(List<Player> players, Location location, float yaw, float pitch);
    void initPacketsRegister(Player player);
    void removePlayers();
    List<BaseComponent> getToolTip(ItemStack itemStack, Player player, boolean advanced);
}
