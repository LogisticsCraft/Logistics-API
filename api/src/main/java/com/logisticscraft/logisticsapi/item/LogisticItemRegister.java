package com.logisticscraft.logisticsapi.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.logisticscraft.logisticsapi.data.LogisticKey;
import com.logisticscraft.logisticsapi.event.ItemRegisterEvent;
import com.logisticscraft.logisticsapi.util.Tracer;

import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LogisticItemRegister {

    private Map<LogisticKey, LogisticItem> itemTypes = new HashMap<>();

    public void registerLogisticItem(@NotNull LogisticItem item) {
        if (itemTypes.putIfAbsent(item.getKey(), item) == null) {
            Tracer.debug("Item register: " + item.getKey());
            Bukkit.getPluginManager().callEvent(new ItemRegisterEvent(item));
        } else {
            Tracer.warn("Trying to reregister kown Item: " + item.getKey());
        }
    }

    public Optional<LogisticItem> getLogisticItem(@NonNull String key) {
        return Optional.ofNullable(itemTypes.get(new LogisticKey(key)));
    }

    public Optional<LogisticItem> getLogisticItem(@NonNull LogisticKey key) {
        return Optional.ofNullable(itemTypes.get(key));
    }

    public Optional<LogisticItem> getLogisticItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return Optional.empty();
        }
        NBTItem nbtItem = new NBTItem(item);
        if (!nbtItem.hasKey("itemid")) {
            return Optional.empty();
        }
        return Optional.ofNullable(itemTypes.get(new LogisticKey(nbtItem.getString("itemid"))));
    }

    public Map<LogisticKey, LogisticItem> getRegisteredItems() {
        return Collections.unmodifiableMap(itemTypes);
    }
}
