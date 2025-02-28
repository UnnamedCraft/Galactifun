package io.github.addoncommunity.galactifun.util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import io.github.thebusybiscuit.slimefun4.libraries.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

/**
 * Utilities
 *
 * @author Seggan
 * @author Mooy1
 */
@UtilityClass
public final class Util {

    public static final double KM_PER_LY = 9.461e12;
    public static final Pattern COORD_PATTERN = Pattern.compile("^-?\\d+ -?\\d+$");
    public static final Pattern SPACE_PATTERN = Pattern.compile(" ");
    public static final BlockFace[] SURROUNDING_FACES = new BlockFace[] {
            BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST,
            BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST
    };
    // TODO maybe move this to protection manager?
    public static final Set<Material> IMPERMEABLE_BLOCKS = EnumSet.noneOf(Material.class);
    public static final BlockFace[] ALL_SIDES = {
            BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN
    };

    static {
        IMPERMEABLE_BLOCKS.addAll(SlimefunTag.GLASS_BLOCKS.getValues());
        IMPERMEABLE_BLOCKS.addAll(SlimefunTag.TERRACOTTA.getValues());
        IMPERMEABLE_BLOCKS.addAll(SlimefunTag.UNBREAKABLE_MATERIALS.getValues());
        IMPERMEABLE_BLOCKS.addAll(Arrays.asList(
                Material.IRON_DOOR,
                Material.IRON_TRAPDOOR,
                Material.OBSIDIAN,
                Material.IRON_BLOCK,
                Material.GOLD_BLOCK,
                Material.DIAMOND_BLOCK,
                Material.NETHERITE_BLOCK,
                Material.SEA_LANTERN,
                Material.QUARTZ_BLOCK,
                Material.SMOOTH_QUARTZ
        ));
        for (Material material : Material.values()) {
            if (material.name().startsWith("WAXED") || material.name().endsWith("CONCRETE")) {
                IMPERMEABLE_BLOCKS.add(material);
            }
        }
    }

    @Nonnull
    public static String timeSince(double nanoTime) {
        nanoTime = System.nanoTime() - nanoTime;
        if (nanoTime >= 1_000_000_000D) {
            return ((int) (nanoTime / 1_000_000D)) / 1000 + " s";
        } else {
            return ((int) (nanoTime / 1_000D)) / 1000D + " ms";
        }
    }

    /**
     * Queue-based flood fill algorithm
     *
     * @param start starting block
     * @param max maximum traversed blocks
     *
     * @return blocks traversed, or an empty optional if traversed blocks > max
     */
    @Nonnull
    public static Optional<Set<BlockPosition>> floodFill(@Nonnull Location start, int max) {
        if (max == 0) return Optional.empty();

        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new ArrayDeque<>();
        queue.add(start.getBlock());

        while (!queue.isEmpty()) {
            if (visited.size() > max) return Optional.empty();

            Block next = queue.remove();
            visited.add(next);
            for (BlockFace face : ALL_SIDES) {
                Block b = next.getRelative(face);
                if (!IMPERMEABLE_BLOCKS.contains(b.getType()) && !visited.contains(b) && !queue.contains(b)) {
                    queue.add(b);
                }
            }
        }

        return Optional.of(visited.parallelStream().map(BlockPosition::new).collect(Collectors.toSet()));
    }

}
