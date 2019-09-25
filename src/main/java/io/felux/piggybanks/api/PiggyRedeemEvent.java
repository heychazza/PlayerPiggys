package io.felux.piggybanks.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PiggyRedeemEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player;
    private UUID ownedBy;
    private int amount;

    public PiggyRedeemEvent(Player player, UUID ownedBy, int amount) {
        this.player = player;
        this.ownedBy = ownedBy;
        this.amount = amount;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getOwnedBy() {
        return ownedBy;
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
