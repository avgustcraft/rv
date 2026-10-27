package dev.reusablevaults;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Vault;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Trial Chamber vaults can be opened infinitely with keys.
 * Vanilla remembers which players already looted a vault; we simply
 * make the vault forget the player before and right after each key use.
 */
public final class ReusableVaults extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("ReusableVaults enabled - vaults can be opened again and again.");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.useInteractedBlock() == Event.Result.DENY) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.VAULT) return;

        ItemStack item = event.getItem();
        if (item == null) return;
        Material type = item.getType();
        if (type != Material.TRIAL_KEY && type != Material.OMINOUS_TRIAL_KEY) return;

        UUID uuid = event.getPlayer().getUniqueId();

        // 1) Clear BEFORE vanilla checks "has this player already looted me?"
        forget(block, uuid);

        // 2) Clear again one tick later, after vanilla re-adds the player.
        Bukkit.getScheduler().runTask(this, () -> forget(block, uuid));
    }

    private void forget(Block block, UUID uuid) {
        if (block.getType() != Material.VAULT) return;
        BlockState state = block.getState();
        if (state instanceof Vault vault && vault.hasRewardedPlayer(uuid)) {
            vault.removeRewardedPlayer(uuid);
            vault.update(true, false);
        }
    }
}
