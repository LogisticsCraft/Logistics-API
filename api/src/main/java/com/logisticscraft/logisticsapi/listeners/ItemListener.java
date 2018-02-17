package com.logisticscraft.logisticsapi.listeners;

import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import com.logisticscraft.logisticsapi.item.LogisticBlockItem;
import com.logisticscraft.logisticsapi.item.LogisticItem;
import com.logisticscraft.logisticsapi.item.LogisticItemRegister;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ItemListener implements Listener {

    @Inject
    private LogisticItemRegister itemRegister;

    @EventHandler(ignoreCancelled=true, priority = EventPriority.LOW)
    public void onPlacer(BlockPlaceEvent event) {
        if(!event.canBuild())return;
        Optional<LogisticItem> logisticItem = itemRegister.getLogisticItem(event.getItemInHand());
        logisticItem.ifPresent(item -> {
            if (item instanceof LogisticBlockItem) {
                ((LogisticBlockItem) item).onPlace(event.getPlayer(), event.getItemInHand(), event.getBlock());
            }
        });
    }

    @EventHandler(ignoreCancelled=true, priority = EventPriority.LOW)
    public void onPlace(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Optional<LogisticItem> logisticItem = itemRegister.getLogisticItem(event.getItem());
            Block block = event.getClickedBlock().getRelative(event.getBlockFace());
            if(logisticItem.isPresent() && block.getType() == Material.AIR && logisticItem.get() instanceof LogisticBlockItem){
                Collection<Entity> entities = block.getWorld().getNearbyEntities(block.getLocation().add(0.5, 0.5, 0.5), 0.5, 0.5, 0.5);
                for(Entity ent : entities){
                    if(ent instanceof LivingEntity){
                        return;
                    }
                }
                if(((LogisticBlockItem) logisticItem.get()).onPlace(event.getPlayer(), event.getItem(), block))
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event){
        Optional<LogisticItem> logisticItem = itemRegister.getLogisticItem(event.getItem());
        logisticItem.ifPresent(item -> {
            if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
                item.onLeftClick(event);
            }else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
                item.onRightClick(event);
            }
        });
    }

    @EventHandler(ignoreCancelled=true, priority = EventPriority.LOW)
    public void onAttack(EntityDamageByEntityEvent event){
        if(event.getCause() == DamageCause.ENTITY_ATTACK && event.getDamager() instanceof Player){
            Optional<LogisticItem> logisticItem = itemRegister.getLogisticItem(((Player)event.getDamager()).getInventory().getItemInMainHand());
            logisticItem.ifPresent(item -> {
                item.onAttack(event);
            });
        }
    }

}
