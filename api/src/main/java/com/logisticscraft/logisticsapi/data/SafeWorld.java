package com.logisticscraft.logisticsapi.data;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.Serializable;
import java.util.Optional;

@Value
@AllArgsConstructor
public class SafeWorld implements Serializable {

    @NonNull
    private String name;

    public SafeWorld(@NonNull final World world) {
        this(world.getName());
    }

    public Optional<World> getWorld() {
        return Optional.ofNullable(Bukkit.getWorld(name));
    }

}
