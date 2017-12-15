package com.logisticscraft.logisticsapi.rewrite.data;

import com.logisticscraft.logisticsapi.rewrite.persistence.Persistent;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Optional;

@Value
@AllArgsConstructor
public class SafeWorld {

    @NonNull @Persistent private final String name;

    public SafeWorld(@NonNull World world) {
        this(world.getName());
    }

    public Optional<World> getWorld() {
        return Optional.ofNullable(Bukkit.getWorld(name));
    }

}
