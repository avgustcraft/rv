package dev.reusablevaults;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Vault;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Trial Chamber vaults can be opened infinitely with keys.
 *
 * Vanilla remembers which players already looted a vault and turns the vault
 * "inactive" for them (inactive vaults ignore keys). So every few ticks we make
 * every vault near a player forget that player, keeping the vault active.
 */
public final class ReusableVaults extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskTimer(this, this::sweep, 20L, 5L);
        getLogger().info("ReusableVaults enabled - vaults can be opened again and again.");
    }

    private void sweep() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            World world = player.getWorld();
            int cx = player.getLocation().getBlockX() >> 4;
            int cz = player.getLocation().getBlockZ() >> 4;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (!world.isChunkLoaded(cx + dx, cz + dz)) continue;
                    Chunk chunk = world.getChunkAt(cx + dx, cz + dz);
                    for (BlockState state : chunk.getTileEntities(b -> b.getType() == Material.VAULT, false)) {
                        if (state instanceof Vault vault && vault.hasRewardedPlayer(uuid)) {
                            vault.removeRewardedPlayer(uuid);
                        }
                    }
                }
            }
        }
    }
}
