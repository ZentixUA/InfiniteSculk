package com.genife.Runnables;

import com.genife.InfiniteSculk;
import com.genife.Managers.SculkManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.genife.Managers.ConfigManager.*;

public class SculkRunnable extends BukkitRunnable {
    private final CommandSender sender;
    private final int length;
    private final SculkManager sculkManager = InfiniteSculk.getInstance().getSculkManager();
    private World world;
    private int xDelta;
    private int zDelta;
    private BlockFace direction;
    private int counter;

    public SculkRunnable(CommandSender sender, int length) {
        this.sender = sender;
        this.length = length;
    }

    @Override
    public void run() {
        counter++;
        Integer taskId = this.getTaskId();

        // Если в списке нет такого распространения, то добавляем его и назначаем переменные
        if (!sculkManager.containsTask(taskId)) {
            Player player = (Player) sender;
            direction = player.getFacing();
            world = player.getWorld();

            List<Location> catalystLocations = new ArrayList<>();

            switch (direction) {
                case NORTH:
                    zDelta = -LINE_DISTANCE;
                    break;
                case EAST:
                    xDelta = LINE_DISTANCE;
                    break;
                case SOUTH:
                    zDelta = LINE_DISTANCE;
                    break;
                case WEST:
                    xDelta = -LINE_DISTANCE;
                    break;
            }

            for (int i = 0; i < length; i++) {
                // Так и надо, X и Z должны быть переставлены местами
                // Так как i начинается с нуля, в первый раз координаты не меняются
                // Во второй - +8 (8), третий - +8 (16), +8 (24) +8 (32)...
                Location catalystLocation = world.getHighestBlockAt(player.getLocation().add(0, -1, 0).add(
                        direction.getModZ() * i * CATALYST_DISTANCE,
                        0,
                        -direction.getModX() * i * CATALYST_DISTANCE)).getLocation();

                while (catalystLocation.getBlock().isLiquid() || catalystLocation.getBlock().getType() == Material.GRASS) {
                    catalystLocation.add(0, -1, 0);
                }

                CompletableFuture<Chunk> catalystChunk = world.getChunkAtAsync(catalystLocation);
                catalystChunk.thenAccept((chunk) -> chunk.addPluginChunkTicket(InfiniteSculk.getInstance()));

                catalystLocation.getBlock().setType(Material.SCULK_CATALYST);

                Location sculkLocation = catalystLocation.clone().add(direction.getModX(), 0, direction.getModZ());
                sculkLocation.getBlock().setType(Material.SCULK);

                catalystLocations.add(catalystLocation);
            }
            sculkManager.addLocations(taskId, catalystLocations);
        }

        for (Location location : sculkManager.getLocations(taskId)) {
            Fox fox = (Fox) world.spawnEntity(location, EntityType.FOX);
            fox.setSilent(true);
            fox.setHealth(0.0);
            fox.remove();
        }
        // если счётчик достиг значения задержки из конфига - сменяем локацию
        if (counter == SPREAD_DELAY) {
            sculkManager.updateLocations(taskId, xDelta, zDelta, world, direction);
            counter = 0;
        }
    }
}
