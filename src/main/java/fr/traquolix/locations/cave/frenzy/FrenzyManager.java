package fr.traquolix.locations.cave.frenzy;

import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.blocks.misc.IndestructibleBedrockBlock;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.RelativeBlockBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.chunk.ChunkUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A manager class responsible for handling the frenzy event in the cave instance.
 */
public class FrenzyManager {

    final Instance instance;
    final Set<Point> frenzyBlocks = new HashSet<>();
    final Map<Point, Block> frenzyBlockOriginals = new HashMap<>();
    final ConcurrentLinkedQueue<Point> sandBlocks = new ConcurrentLinkedQueue<>();
    int frenzyTime = 60;

    boolean frenzyEnded = false;

    /**
     * Constructor to initialize the frenzy manager for the given instance.
     *
     * @param instance The Minestom instance for which to manage the frenzy event.
     */
    public FrenzyManager(Instance instance) {
        this.instance = instance;
        registerFrenzyListener();
    }

    private void registerFrenzyListener() {
        // Create a player event listener node for the frenzy event
        EventNode<PlayerEvent> frenzyListener = EventNode.type("frenzy-collection-listener", EventFilter.PLAYER);

        // Add a player block break event listener to handle the frenzy event
        EventListener<PlayerBlockBreakEvent> frenzyEvent = EventListener.builder(PlayerBlockBreakEvent.class)
                .expireWhen(event -> frenzyEnded) // Stop when frenzy started
                .handler(event -> sandBlocks.forEach((pos) -> {
                    // Increase frenzy time when players break sand blocks during the frenzy
                    if (event.getBlockPosition().sameBlock(pos)) {
                        frenzyTime += 5;
                        event.getPlayer().sendMessage(Component.text("Frenzy time is now: " + frenzyTime + " seconds"));
                    }
                }))
                .build();

        frenzyListener.addListener(frenzyEvent);

        // Add the frenzy listener node to the global event handler
        MinecraftServer.getGlobalEventHandler().addChild(frenzyListener);
    }

    /**
     * Add extra time to the frenzy event.
     *
     * @param time The amount of extra time to add to the frenzy event.
     */
    public synchronized void addTime(int time) {
        frenzyTime += time;
    }

    /**
     * Add a frenzy block to the frenzy event.
     *
     * @param pos   The position of the frenzy block.
     * @param block The original block type at the frenzy block position.
     */
    public void addFrenzyBlock(Point pos, Block block) {
        frenzyBlocks.add(pos);
        frenzyBlockOriginals.put(pos, block);
    }

    /**
     * Add a sand block to the frenzy event.
     *
     * @param pos The position of the sand block.
     */
    public void addSandBlock(Point pos) {
        sandBlocks.add(pos);
    }

    /**
     * Start the frenzy event.
     */
    public synchronized void startFrenzy() {
        // Load all chunks and regenerate all frenzy blocks
        RelativeBlockBatch batch = new RelativeBlockBatch();
        Set<Long> chunkToLoadList = new HashSet<>();
        frenzyBlockOriginals.forEach((pos, block) -> {
            batch.setBlock(pos, block);
            chunkToLoadList.add(ChunkUtils.getChunkIndex(pos));
        });
        ChunkUtils.optionalLoadAll(instance, chunkToLoadList.stream().mapToLong(l -> l).toArray(), null)
                .thenRun(() -> batch.apply(instance, null));

        // Start the frenzy event and add an event to replace broken frenzy blocks with bedrock
        EventListener<PlayerBlockBreakEvent> frenzyEvent = EventListener.builder(PlayerBlockBreakEvent.class)
                .expireWhen(event -> frenzyEnded) // Stop when frenzy started
                .handler(event -> frenzyBlocks.forEach((pos) -> {
                    if (event.getBlockPosition().sameBlock(pos)) {
                        event.setCancelled(true);
                        Block block = event.getBlock();
                        instance.setBlock(pos, BlockRegistry.getInstance().getBlock(IndestructibleBedrockBlock.identifier).getNaturalBlock());
                        MinecraftServer.getSchedulerManager().scheduleTask(() -> instance.setBlock(event.getBlockPosition(), block), TaskSchedule.seconds(5), TaskSchedule.stop());
                    }
                }))
                .build();

        // Add the event listener to a player event listener node
        EventNode<PlayerEvent> frenzyListener = EventNode.type("frenzy-replacement-listener", EventFilter.PLAYER);
        frenzyListener.addListener(frenzyEvent);

        // Add the frenzy listener node to the global event handler
        MinecraftServer.getGlobalEventHandler().addChild(frenzyListener);

        // Start the countdown for the frenzy event
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            frenzyTime--;
            //instance.audiences().forEach(player -> player.sendMessage(
            //        Component.text("Frenzy time left: " + frenzyTime + " seconds")));
            if (frenzyTime <= 0) {
                frenzyEnded = true;
                return TaskSchedule.stop();
            } else {
                return TaskSchedule.seconds(1);
            }
        });
    }
}
