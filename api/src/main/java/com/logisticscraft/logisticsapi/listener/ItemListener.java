package com.logisticscraft.logisticsapi.listener;

import com.logisticscraft.logisticsapi.item.LogisticBlockItem;
import com.logisticscraft.logisticsapi.item.LogisticItemRegister;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ItemListener implements Listener {

    @Inject
    private LogisticItemRegister itemRegister;

    @EventHandler(priority = EventPriority.LOW)
    public void onPlacer(BlockPlaceEvent event) {
        if (!event.canBuild()) {
            return;
        }
        itemRegister.getLogisticItem(event.getItemInHand()).ifPresent(item -> {
            if (item instanceof LogisticBlockItem) {
                ((LogisticBlockItem) item).onPlace(event.getPlayer(), event.getItemInHand(), event.getBlock());
            }
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlace(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        itemRegister.getLogisticItem(event.getItem()).ifPresent(item -> {
            Block block = event.getClickedBlock().getRelative(event.getBlockFace());
            if (block.getType() == Material.AIR && item instanceof LogisticBlockItem) {
                Collection<Entity> entities = block.getWorld().getNearbyEntities(block.getLocation().add(0.5, 0.5, 0.5), 0.5, 0.5, 0.5);
                for (Entity currentEntity : entities) {
                    if (currentEntity instanceof LivingEntity) {
                        return;
                    }
                }
                if (((LogisticBlockItem) item).onPlace(event.getPlayer(), event.getItem(), block)) {
                    event.setCancelled(true);
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        itemRegister.getLogisticItem(event.getItem()).ifPresent(item -> {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                item.onLeftClick(event);
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                item.onRightClick(event);
            }
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getCause() != DamageCause.ENTITY_ATTACK || !(event.getDamager() instanceof Player)) {
            return;
        }
        itemRegister.getLogisticItem(((Player) event.getDamager()).getInventory().getItemInMainHand())
                .ifPresent(item -> item.onAttack(event));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBreak(BlockBreakEvent event) {
        itemRegister.getLogisticItem(event.getPlayer().getInventory().getItemInMainHand())
                .ifPresent(item -> item.onBreak(event));
    }
}
