package gg.plugins.piggybanks.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BagRedeemEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private OfflinePlayer player;
    private int amount;

    public BagRedeemEvent(OfflinePlayer player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
