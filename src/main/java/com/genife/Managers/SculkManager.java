package com.genife.Managers;

import com.genife.InfiniteSculk;
import org.bukkit.*;
import org.bukkit.block.BlockFace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SculkManager {
    private final InfiniteSculk instance = InfiniteSculk.getInstance();
    private final Map<Integer, List<Location>> catalystLocations;

    public SculkManager() {
        catalystLocations = new HashMap<>();
    }

    public void addLocations(int taskId, List<Location> locations) {
        catalystLocations.put(taskId, locations);
    }

    public void updateLocations(int taskId, int xDelta, int zDelta, World world, BlockFace direction) {
        List<Location> locationsList = catalystLocations.get(taskId);

        for (Location oldLocation : locationsList) {
            // Удаляем тикет старого чанка
            CompletableFuture<Chunk> oldChunk = world.getChunkAtAsync(oldLocation);
            oldChunk.thenAccept(chunk -> world.getChunkAtAsync(oldLocation, chunk.removePluginChunkTicket(instance)));

            // Высчитываем новую локацию для скалк-каталиста.
            Location catalystLocation = world.getHighestBlockAt(oldLocation.clone().add(xDelta, 0, zDelta)).getLocation();

            while (catalystLocation.getBlock().isLiquid() || catalystLocation.getBlock().getType() == Material.GRASS) {
                catalystLocation.add(0, -1, 0);
            }

            catalystLocation.getBlock().setType(Material.SCULK_CATALYST);

            // Высчитываем локацию для скалк-блока
            Location sculkLocation = catalystLocation.clone().add(direction.getModX(), 0, direction.getModZ());
            sculkLocation.getBlock().setType(Material.SCULK);

            // хоть и сам oldLocation изменился, но изменения отобразились в списках наших, потому он находит индекс
            locationsList.set(locationsList.indexOf(oldLocation), catalystLocation);

            // добавляем тикет нового чанка
            CompletableFuture<Chunk> newChunk = world.getChunkAtAsync(catalystLocation);
            newChunk.thenAccept(chunk -> world.getChunkAtAsync(catalystLocation, chunk.addPluginChunkTicket(instance)));
        }
    }

    public void stopTasks() {
        // отменяем каждую раннаблу скалка
        for (int task : catalystLocations.keySet()) {
            Bukkit.getScheduler().cancelTask(task);
        }

        // удаляем тикет чанков, которые были для скалка
        for (List<Location> locations : catalystLocations.values()) {
            for (Location location : locations) {
                CompletableFuture<Chunk> futureChunk = location.getWorld().getChunkAtAsync(location);
                futureChunk.thenAccept(chunk -> chunk.removePluginChunkTicket(instance));
            }
        }
        // чистим список
        catalystLocations.clear();
    }

    public boolean containsTask(Integer taskId) {
        return catalystLocations.containsKey(taskId);
    }

    public List<Location> getLocations(Integer taskId) {
        return catalystLocations.get(taskId);
    }
}