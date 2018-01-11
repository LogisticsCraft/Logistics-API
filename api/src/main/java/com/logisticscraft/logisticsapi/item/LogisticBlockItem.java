package com.logisticscraft.logisticsapi.item;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.logisticscraft.logisticsapi.LogisticsApi;
import com.logisticscraft.logisticsapi.block.LogisticBlock;
import com.logisticscraft.logisticsapi.data.LogisticKey;

import lombok.Getter;
import lombok.NonNull;

public class LogisticBlockItem extends LogisticItem {

    @Getter
    private final Class<? extends LogisticBlock> representingBlock;
    @Getter
    private final LogisticKey representingBlockKey;

    public LogisticBlockItem(@NonNull Plugin plugin, @NonNull String name,
            @NonNull Class<? extends LogisticBlock> logisticBlock, @NonNull ItemStack baseStack) {
        super(plugin, name, baseStack);
        this.representingBlock = logisticBlock;
        this.representingBlockKey = LogisticsApi.getInstance().getBlockManager().getKey(logisticBlock).get();
    }

    public LogisticBlockItem(Class<? extends LogisticBlock> logisticBlock, ItemStack baseStack) {
        this(LogisticsApi.getInstance().getBlockManager().getKey(logisticBlock).get().getPlugin().get(),
                LogisticsApi.getInstance().getBlockManager().getKey(logisticBlock).get().getName(), logisticBlock,
                baseStack);
    }

    public void onPlace(Player player, @NonNull ItemStack item, @NonNull Block block) {
        LogisticsApi.getInstance().getBlockManager().placeLogisticBlock(player, item, block.getLocation(),
                representingBlockKey);
    }

}
