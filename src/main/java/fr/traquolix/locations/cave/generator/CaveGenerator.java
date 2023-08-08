package fr.traquolix.locations.cave.generator;

import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.generators.noise_parameters.interpolation.Interpolation;
import de.articdive.jnoise.modules.combination.CombinationModule;
import de.articdive.jnoise.modules.combination.Combiner;
import de.articdive.jnoise.modules.octavation.OctavationModule;
import de.articdive.jnoise.pipeline.JNoise;
import fr.traquolix.commands.DwarfCabinCommand;
import fr.traquolix.commands.FrenzyReceptacleCommand;
import fr.traquolix.commands.ObservatoryCommand;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.blocks.misc.CloudBlock;
import fr.traquolix.content.blocks.misc.IndestructibleCoalBlock;
import fr.traquolix.content.blocks.misc.SandOfTimeBlock;
import fr.traquolix.content.blocks.ores.BloodstoneOreBlock;
import fr.traquolix.locations.cave.frenzy.FrenzyManager;
import fr.traquolix.locations.cave.generator.decorations.VineGenerator;
import fr.traquolix.locations.cave.generator.structures.DwarfCabinStructure;
import fr.traquolix.locations.cave.generator.structures.FrenzyReceptacleStructure;
import fr.traquolix.locations.cave.generator.structures.ObservatoryStructure;
import fr.traquolix.locations.cave.generator.structures.Structure;
import fr.traquolix.locations.cave.generator.structures.planets.MoonStructure;
import fr.traquolix.locations.cave.generator.structures.planets.PlanetStructure;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.chunk.ChunkUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.traquolix.Main.logger;

/**
 * A custom generator to create cave-like terrain with ores and rare blocks.
 */
public class CaveGenerator implements Generator {

    public final Instance instance;
    public static double caveSizeZ = 300;
    public static double caveSizeY = 200;
    public static double caveSizeX = 300;
    public static double snowStartHeight = caveSizeY - 60;
    public static double peakStartHeight = caveSizeY - 30;
    public static double maxHeightSize = 320;
    Random random = new Random();
    int seed = random.nextInt();
    FrenzyManager frenzyManager;
    @Getter
    List<Structure> structures = new ArrayList<>();
    final VineGenerator vineGenerator;
    Point highestPoint = new Pos(-1000, -1000, -1000);
    double highestHeight = -1000;

    Point telescopePoint;

    // Configure noise modules for cave terrain generation
    final JNoise noiseA = JNoise.newBuilder().perlin(seed, Interpolation.LINEAR, FadeFunction.IMPROVED_PERLIN_NOISE)
            .scale(1.0 / 100.0)  // Use a larger divisor for a larger "scale" (i.e., lower frequency)
            .clamp(-1.0, 1.0)
            .build();

    final JNoise noiseB = JNoise.newBuilder().perlin(seed, Interpolation.LINEAR, FadeFunction.IMPROVED_PERLIN_NOISE)
            .scale(1.0 / 50.0)  // Use a smaller divisor for a smaller "scale" (i.e., higher frequency)
            .clamp(-1.0, 1.0)
            .build();

    final OctavationModule octavationModuleA = OctavationModule.newBuilder().setNoiseSource(noiseA).setOctaves(2).setPersistence(0.5).setLacunarity(2).build();
    final OctavationModule octavationModuleB = OctavationModule.newBuilder().setNoiseSource(noiseB).setOctaves(2).setPersistence(0.5).setLacunarity(2).build();
    final CombinationModule combinationModule = CombinationModule.newBuilder().setA(octavationModuleA).setB(octavationModuleB).setCombiner(Combiner.MAX).build();

    final JNoise veins = JNoise.newBuilder().perlin(seed, Interpolation.COSINE, FadeFunction.IMPROVED_PERLIN_NOISE)
            .scale(1.0 / 20.0)  // Use a smaller divisor for a smaller "scale" (i.e., higher frequency)
            .clamp(-1.0, 1.0)
            .build();

    final JNoise elevationNoise = JNoise.newBuilder().perlin(seed, Interpolation.CUBIC, FadeFunction.IMPROVED_PERLIN_NOISE)
            .scale(1.0 / 50.0)  // Use a smaller divisor for a smaller "scale" (i.e., higher frequency)
            .clamp(-1.0, 1.0)
            .build();

    final JNoise elevationNoise2 = JNoise.newBuilder().perlin(seed, Interpolation.CUBIC, FadeFunction.IMPROVED_PERLIN_NOISE)
            .scale(1.0 / 40.0)  // Use a smaller divisor for a smaller "scale" (i.e., higher frequency)
            .clamp(-1.0, 1.0)
            .build();

    final OctavationModule elevationNoiseOct = OctavationModule.newBuilder().setNoiseSource(elevationNoise).setOctaves(3).setPersistence(0.5).setLacunarity(2).build();

    final OctavationModule elevationNoiseOct2 = OctavationModule.newBuilder().setNoiseSource(elevationNoise2).setOctaves(3).setPersistence(0.5).setLacunarity(2).build();
    final CombinationModule peaksNoise = CombinationModule.newBuilder().setA(elevationNoiseOct).setB(elevationNoise2).setCombiner(Combiner.MAX).build();

    final CombinationModule cloudsNoise = CombinationModule.newBuilder().setA(peaksNoise).setB(elevationNoiseOct2).setCombiner(Combiner.ADD).build();
    /**
     * Constructor to initialize the cave generator for the given instance.
     *
     * @param instance The Minestom instance for which to generate the caves.
     */
    public CaveGenerator(Instance instance) {
        this.instance = instance;

        frenzyManager = new FrenzyManager(instance);
        structures.add(new DwarfCabinStructure(instance));
        structures.add(new FrenzyReceptacleStructure(instance));
        structures.add(new ObservatoryStructure(instance));
        structures.add(new MoonStructure(instance));
        vineGenerator = new VineGenerator(instance);

        MinecraftServer.getSchedulerManager().scheduleTask(
                frenzyManager::startFrenzy, TaskSchedule.minutes(1), TaskSchedule.stop());
    }

    /**
     * Generate cave-like terrain with ores and rare blocks in the given generation unit.
     *
     * @param unit The generation unit for which to generate terrain.
     */
    public void generate(@NotNull GenerationUnit unit) {

        // TODO Rajouter une génération de tuff pour rendre le visuel un peu plus intéressant dans les caves.
        Point start = unit.absoluteStart();

        double flatnessThreshold = 1;

        int MAX_X = unit.size().blockX();
        int MAX_Y = unit.size().blockY();
        int MAX_Z = unit.size().blockZ();

        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                for (int z = 0; z < MAX_Z; z++) {
                    Point current = start.add(x, y, z);

                    // Skip generation outside the defined cave area
                    if (Math.abs(current.x()) > caveSizeX || Math.abs(current.z()) > caveSizeZ || current.y() < 0) {
                        //unit.modifier().setBlock(current, Block.AIR);
                        continue;
                    }

                    // Generate indestructible_coal_block for the borders of the cave area
                    if (current.y() == 0 || Math.abs(current.x()) == caveSizeX || Math.abs(current.z()) == caveSizeZ /*|| current.y() == 200*/) {
                        unit.modifier().setBlock(current, BlockRegistry.getInstance().getBlock(IndestructibleCoalBlock.identifier).getNaturalBlock());
                        continue;
                    }

                    // Calculate noise values for terrain generation
                    double noise = combinationModule.evaluateNoise(current.x(), current.y(), current.z());
                    double veinsNoise = veins.evaluateNoise(current.x(), current.y(), current.z());
                    double sandNoise = veins.evaluateNoise(current.y(), current.z(), current.x());
                    double blackStoneNoise = combinationModule.evaluateNoise(current.y(), current.z(), current.x());
                    double iceNoise = combinationModule.evaluateNoise(current.x(), current.z(), current.y());
                    double emeraldAndLapisNoise = veins.evaluateNoise(current.z(), current.x(), current.y());


                    if (current.y() >= snowStartHeight && current.y() <= peakStartHeight) {
                        if (current.y() == snowStartHeight && (noise > 0.000007)) {
                            int value = random.nextInt(6);
                            for (int i = -value; i <= 0; i++) {
                                if ((iceNoise > 0.0000001 && iceNoise < 0.05)) {
                                    unit.modifier().setBlock(current.add(0, i, 0), Block.ICE);
                                } else {
                                    unit.modifier().setBlock(current.add(0, i, 0), Block.SNOW_BLOCK);
                                }
                            }
                            continue;
                        }
                        if (current.y() == peakStartHeight/* && noise > 0.00001*/) {
                            double noiseHeight = peaksNoise.evaluateNoise(current.x(), current.z());
                            double height = Math.pow(Math.abs(noiseHeight)*7, 2);

                            height = Math.ceil(height);
                            if (highestHeight < height
                            && Math.abs(current.x()) < caveSizeX-25
                            && Math.abs(current.z()) < caveSizeZ-25) {
                                highestHeight = height;
                                highestPoint = current.withY(current.y() + height);
                            }
                            if (height <= 1) {
                                continue;
                            }
                            for (double i = -height+2; i < height; i++) {
                                if (i == height-1) {
                                    unit.modifier().setBlock(current.add(0, i, 0),
                                            Block.SNOW.withProperty("layers",
                                                    String.valueOf(random.nextInt(2, 9))));
                                } else {
                                    if (height > 3 && i < height-10 && i > 0) {
                                        unit.modifier().setBlock(current.add(0, i, 0), Block.BLUE_ICE);
                                    } else {
                                        unit.modifier().setBlock(current.add(0, i, 0), Block.SNOW_BLOCK);
                                    }
                                }
                            }
                            continue;
                        }
                        if ((iceNoise > 0.0000001 && iceNoise < 0.05) && (noise > 0.00001)) {
                            unit.modifier().setBlock(current, Block.ICE);
                            continue;
                        } else if (noise > 0.00001) {
                            unit.modifier().setBlock(current, Block.SNOW_BLOCK);
                            continue;
                        }
                    }



                    if (current.y() == 250) {
                        // We can even make it so it acts like your going down into it, like quicksand or clouds from the aether mod.

                        double noiseHeight = cloudsNoise.evaluateNoise(current.z(), current.x());
                        double height = Math.abs(noiseHeight)*4;


                        if (height <= 1) {
                            continue;
                        }
                        for (double i = -height+2; i < height; i++) {
                            unit.modifier().setBlock(current.add(0, i, 0), BlockRegistry.getInstance().getBlock(CloudBlock.identifier).getNaturalBlock());

                        }
                        continue;
                    }

                    if (current.y() > snowStartHeight) {
                        continue;
                    }


                    if (current.y() == 1 && !(noise > 0.0000001)) {
                        switch (random.nextInt(10)) {
                            case 0 -> unit.modifier().setBlock(start.add(x, y, z), Block.GILDED_BLACKSTONE);
                            case 1 -> unit.modifier().setBlock(start.add(x, y, z), Block.CRACKED_POLISHED_BLACKSTONE_BRICKS);
                            case 2 -> unit.modifier().setBlock(start.add(x, y, z), Block.POLISHED_BLACKSTONE_BRICKS);
                            default -> unit.modifier().setBlock(start.add(x, y, z), Block.LAVA);
                        }
                        continue;
                    }

                    // Generate rare ores and blocks based on noise values
                    if ((current.y() > caveSizeY - 49 && current.y() < caveSizeY) && (emeraldAndLapisNoise > 0.85 && (noise > 0.0000001 && noise < 0.02))) {
                        unit.modifier().setBlock(current, Block.LAPIS_BLOCK);
                    } else if ((current.y() > 1 && current.y() < 50) && (emeraldAndLapisNoise > 0.85 && (noise > 0.0000001 && noise < 0.02))) {
                        unit.modifier().setBlock(current, Block.EMERALD_BLOCK);
                    } else if ((noise > 0.0000001 && noise < 0.01) && veinsNoise > 0.85 && (current.y() >= 50 && current.y() <= caveSizeY-50)) {
                        Block block = BlockRegistry.getInstance().getBlock(BloodstoneOreBlock.identifier).getNaturalBlock();
                        unit.modifier().setBlock(current, block);
                        frenzyManager.addFrenzyBlock(current, block);
                    } else if ((noise > 0.0000001 && noise < 0.02) && veinsNoise > 0.65 && (current.y() >= 50 && current.y() <= caveSizeY-50)) {
                        unit.modifier().setBlock(current, Block.GOLD_BLOCK);
                    } else if (sandNoise > 0.9 && (noise > 0.0000001 && noise < 0.01)) {
                        unit.modifier().setBlock(current, BlockRegistry.getInstance().getBlock(SandOfTimeBlock.identifier).getNaturalBlock());
                        frenzyManager.addSandBlock(current);
                    } else if ((blackStoneNoise > 0.0000001 && blackStoneNoise < 0.05) && (noise > 0.0000001 && noise < 0.1)) {
                        unit.modifier().setBlock(current, Block.BLACKSTONE);
                    } else if (noise > 0.0000001 && noise < 0.0001 && current.y() < snowStartHeight-10) {
                        Point gradient = computeGradient(current);
                        if (Math.abs(gradient.y()) > Math.abs(gradient.x()) && Math.abs(gradient.y()) > Math.abs(gradient.z())) {
                            if (gradient.y() > 0) {
                                int maxVineHeight = Math.min(Math.abs(current.blockY()), 5); // It won't exceed 5 and won't go below y=0
                                int vineHeight = random.nextInt(maxVineHeight + 1);
                                vineGenerator.addVine(current, vineHeight);
                            }
                        }
                    } else if (noise > 0.0000001 && noise < 0.001) {
                        Point gradient = computeGradient(current);
                        if (Math.abs(gradient.y()) > Math.abs(gradient.x()) && Math.abs(gradient.y()) > Math.abs(gradient.z()) && gradient.y() < 0) {
                            // The gradient is primarily vertical and positive; this is a floor
                            if (isFlat(current, flatnessThreshold)) {
                                if (current.y() > 5
                                        && current.y() < caveSizeY-25
                                        && Math.abs(current.x()) < caveSizeX-25
                                        && Math.abs(current.z()) < caveSizeZ-25) {
                                    for (Structure structure : structures) {
                                        if (structure instanceof FrenzyReceptacleStructure || structure instanceof DwarfCabinStructure) {
                                            structure.getPossibleLocations().add(current);
                                        }
                                    }
                                }
                            } else {
                                unit.modifier().setBlock(current, Block.DEEPSLATE);
                            }
                        }
                    } else if (noise > 0.0000001) {
                        unit.modifier().setBlock(current, Block.DEEPSLATE);
                    } else {
                        unit.modifier().setBlock(current, Block.AIR);
                    }
                }
            }
        }
    }


    private Point computeGradient(Point current) {
        double epsilon = 0.0001;  // Small value for numerical differentiation

        double dx = combinationModule.evaluateNoise(current.x() + epsilon, current.y(), current.z()) -
                combinationModule.evaluateNoise(current.x() - epsilon, current.y(), current.z());

        double dy = combinationModule.evaluateNoise(current.x(), current.y() + epsilon, current.z()) -
                combinationModule.evaluateNoise(current.x(), current.y() - epsilon, current.z());

        double dz = combinationModule.evaluateNoise(current.x(), current.y(), current.z() + epsilon) -
                combinationModule.evaluateNoise(current.x(), current.y(), current.z() - epsilon);

        return new Pos(dx, dy, dz);  // Assuming Point can hold doubles; adjust as necessary
    }


    private boolean isFlat(Point current, double threshold) {
        int radius = 10; // Adjust the radius based on the size of the local neighborhood
        int count = 0;

        double centerNoise = combinationModule.evaluateNoise(current.x(), current.y(), current.z());

        // Check gradient in the vertical direction (Y-axis)
        double noiseAbove = combinationModule.evaluateNoise(current.x(), current.y() + 1, current.z());
        double noiseBelow = combinationModule.evaluateNoise(current.x(), current.y() - 1, current.z());

        double gradient = noiseAbove - noiseBelow;

        // If gradient is too steep in either direction, it's not flat ground
        if(Math.abs(gradient) > threshold) {
            return false;
        }

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dy == 0 && dx == 0 && dz == 0) continue; // Skip the center point

                    Point neighbor = current.add(dx, dy, dz);
                    double neighborNoise = combinationModule.evaluateNoise(neighbor.x(), neighbor.y(), neighbor.z());

                    if (Math.abs(neighborNoise - centerNoise) <= threshold) {
                        count++;
                    }
                }
            }
        }

        // If most of the neighbors have similar noise values, it's considered flat
        return count >= (2 * radius + 1) * (2 * radius + 1) * (2 * radius + 1) / 2;
    }


    /**
     * Generate terrain for all units in the given collection.
     *
     * @param units The collection of generation units to fill.
     */
    @Override
    public void generateAll(@NotNull Collection<@NotNull GenerationUnit> units) {
        units.forEach(this::generate);
    }

    public void populate() {
        preLoad().thenRun(() -> {
            logger.info("[Completion] - Cave chunks preloading");
            buildStructures();
            loadLocationInCommands();
            // TODO Generate a random planet
            // TODO Add elements (gas pockets, etc.)
            logger.info("--- Generation finished ---");
        });
    }

    private void loadLocationInCommands() {
        for (Structure structure : structures) {
            if (structure instanceof ObservatoryStructure) {
                ObservatoryCommand.observatoryPosition = structure.getPlacedStructure();
            } else if (structure instanceof FrenzyReceptacleStructure) {
                FrenzyReceptacleCommand.frenzyReceptaclePosition = structure.getPlacedStructure();
            } else if (structure instanceof DwarfCabinStructure) {
                DwarfCabinCommand.dwarfCabinPosition = structure.getPlacedStructure();
            }
        }
        logger.info("[Completion] - Cave locations loaded in commands");
    }

    private @NotNull CompletableFuture<Void> preLoad() {

        double safetyNet = 100;

        Set<Long> chunkToLoadList = new HashSet<>();
        for (double x = -caveSizeX-safetyNet; x < caveSizeX+safetyNet; x++) {
            for (double y = 0; y < maxHeightSize; y++) {
                for (double z = -caveSizeZ-safetyNet; z < caveSizeZ+safetyNet; z++) {
                    chunkToLoadList.add(ChunkUtils.getChunkIndex(new Pos(x, y, z)));
                }
            }
        }
        AtomicInteger totalChunkCount = new AtomicInteger(chunkToLoadList.size());
        AtomicInteger currentChunkCount = new AtomicInteger(0);
        logger.info("[Information] - Total chunk to load : " + totalChunkCount.get());
        return ChunkUtils.optionalLoadAll(instance, chunkToLoadList.stream().mapToLong(l -> l).toArray(), (callback) -> {
            currentChunkCount.getAndIncrement();
            if (currentChunkCount.get() % 100 == 0) {
                logger.info("[Loading] - " + currentChunkCount.get() + " / " + totalChunkCount.get() + " chunks loaded");
            }
        });
    }

    private void buildStructures() {

        for (Structure structure : structures) {
            buildSingleStructure(structure);
            logger.info("[Information] - " + structure.getClass().getSimpleName() + " placed at " + structure.getPlacedStructure() + " with orientation " + structure.getOrientation());
        }
        logger.info("[Completion] - Cave structures");

        vineGenerator.generateVines();
        logger.info("[Completion] - Cave vines");
    }

    private void buildSingleStructure(Structure structure) {

        prepopulate();

        if (structure.getPlacedStructure() == null) {
            Iterator<Point> pointIterator = structure.getPossibleLocations().iterator();

            while (pointIterator.hasNext()) {
                Point point = pointIterator.next();

                if (structure.canPlaceStructureAt(point)) {
                    synchronized (structure) {
                        if (structure.getPlacedStructure() == null) {
                            structure.setPlacedStructure(point);
                            structure.generateStructure(point);
                        }
                    }
                }

                pointIterator.remove();  // Remove the point from the list after reading
            }
        }
    }

    private void prepopulate() {
        for (Structure structure : structures) {
            if (structure instanceof ObservatoryStructure) {
                if (highestPoint != null) {
                    structure.getPossibleLocations().add(highestPoint);
                }
                Point telescopePointingAt = ((ObservatoryStructure) structure).getTelescopePointingAt();
                if (telescopePointingAt != null) {
                    telescopePoint = telescopePointingAt;
                }
            }

            if (structure instanceof PlanetStructure) {
                if (telescopePoint != null) {
                    structure.getPossibleLocations().add(telescopePoint);
                }
            }
        }
        logger.info("[Completion] - Cave prepopulation");
    }
}