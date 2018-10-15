package com.logisticscraft.logisticsapi.block;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;

public class BasicBlockFactory implements LogisticBlockFactory {

    private Constructor<? extends LogisticBlock> constructor;
    private Object[] staticArgs;

    public BasicBlockFactory(@NonNull Class<? extends LogisticBlock> logisticClass) {
        this(logisticClass, new Class<?>[0], new Object[0]);
    }

    public BasicBlockFactory(@NonNull Class<? extends LogisticBlock> logisticClass, @NonNull Class<?>[] constructor, @NonNull Object[] staticArgs) {
        try {
            Constructor<? extends LogisticBlock> con = logisticClass.getConstructor(constructor);
            con.setAccessible(true);
            this.constructor = con;
            this.staticArgs = staticArgs;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public LogisticBlock onPlace(Player player, ItemStack item, Location location) {
        if (constructor != null) {
            try {
                return constructor.newInstance(staticArgs);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public LogisticBlock onLoad() {
        if (constructor != null) {
            try {
                return constructor.newInstance(staticArgs);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
