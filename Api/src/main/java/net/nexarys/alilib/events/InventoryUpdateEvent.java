package net.nexarys.alilib.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InventoryUpdateEvent extends Event {
    private final Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public InventoryUpdateEvent(Player player) {
       super(true);
       this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public Player getPlayer(){
        return player;
    }
}
