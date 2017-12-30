package com.logisticscraft.logisticsapi.item;

import com.logisticscraft.logisticsapi.data.LogisticBlockFace;
import com.logisticscraft.logisticsapi.data.holder.PersistentDataHolder;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

public interface ItemStorage extends PersistentDataHolder {

    ItemStack extractItem(@NonNull LogisticBlockFace extractionSide, int maxExtractAmount, boolean simulate);

    ItemStack insertItem(@NonNull LogisticBlockFace insertSide, @NonNull ItemStack insertedItem, boolean simulate);

    int howMuchSpaceForItemAsync(@NonNull LogisticBlockFace insertDirection, @NonNull ItemStack insertion);

    default boolean allowItemInsertion(@NonNull LogisticBlockFace blockFace, @NonNull ItemStack item) {
        return true;
    }

    default boolean allowItemExtraction(@NonNull LogisticBlockFace blockFace, @NonNull ItemStack item) {
        return true;
    }

}
