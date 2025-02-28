package io.github.addoncommunity.galactifun.base.items.rockets;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import io.github.addoncommunity.galactifun.api.items.Rocket;
import io.github.addoncommunity.galactifun.api.universe.attributes.atmosphere.Gas;
import io.github.addoncommunity.galactifun.base.items.AssemblyTable;
import io.github.addoncommunity.galactifun.core.CoreItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

public final class IonRocket extends Rocket {

    public IonRocket(SlimefunItemStack item, ItemStack[] recipe, int fuelCapacity, int storageCapacity) {
        super(CoreItemGroup.ITEMS, item, AssemblyTable.TYPE, recipe, fuelCapacity, storageCapacity);
    }

    @Override
    protected List<ItemStack> getAllowedFuels() {
        return List.of(Gas.ARGON.item());
    }

    @Nonnull
    @Override
    protected Particle getLaunchParticles() {
        return Particle.SOUL_FIRE_FLAME;
    }

}
