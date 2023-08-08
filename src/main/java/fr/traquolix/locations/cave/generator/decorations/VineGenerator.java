package fr.traquolix.locations.cave.generator.decorations;


import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.RelativeBlockBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.chunk.ChunkUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VineGenerator {
    final Instance instance;
    final ConcurrentMap<Point, Integer> vinePoints = new ConcurrentHashMap<>();
    public VineGenerator(Instance instance) {
        this.instance = instance;
    }


    public void generateVines() {

        RelativeBlockBatch batch = new RelativeBlockBatch();
        Set<Long> chunkToLoadList = new HashSet<>();

        for (Map.Entry<Point, Integer> entry : vinePoints.entrySet()) {
            Point point = entry.getKey();
            chunkToLoadList.add(ChunkUtils.getChunkIndex(point));
            int height = entry.getValue();
            for (int i = 0; i < height; i++) {
                batch.setBlock(point.withY(point.blockY() - i), Block.VINE);
            }
        }
        ChunkUtils.optionalLoadAll(instance, chunkToLoadList.stream().mapToLong(l -> l).toArray(), null)
                .thenRun(() -> batch.apply(instance, null));
        vinePoints.clear();
    }

    public void addVine(Point current, int i) {
        this.vinePoints.put(current, i);
    }
}
