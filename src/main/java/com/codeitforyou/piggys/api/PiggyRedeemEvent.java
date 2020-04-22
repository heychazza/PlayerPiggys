package com.codeitforyou.piggys.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PiggyRedeemEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player;
    private UUID ownedBy;
    private long amount;
    private ItemStack item;
    private PiggySlot slot;

    public PiggyRedeemEvent(Player player, UUID ownedBy, long amount, ItemStack item, PiggySlot slot) {
        this.player = player;
        this.ownedBy = ownedBy;
        this.amount = amount;
        this.item = item;
        this.slot = slot;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getOwnedBy() {
        return ownedBy;
    }

    public long getAmount() {
        return amount;
    }

    public ItemStack getItem() {
        return item;
    }

    public PiggySlot getSlot() {
        return slot;
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
