package fr.traquolix.locations.cave.generator.structures;

import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.blocks.misc.CloudBlock;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.locations.cave.generator.CaveGenerator;
import fr.traquolix.locations.cave.generator.structures.helpers.RaycastResult;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.RelativeBlockBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static fr.traquolix.Main.logger;
@Getter
public abstract class Structure {

    Instance instance;
    @Getter
    @Setter
    Point placedStructure = null;
    @Getter
    Set<Point> possibleLocations = Collections.newSetFromMap(new ConcurrentHashMap<>());
    @Getter
    int halfStructureSize = 5;
    @Getter
    int structureHeight = 9;
    @Getter
    int orientation = 0; // 0 = North, 1 = East, 2 = South, 3 = West
    protected RelativeBlockBatch batch = new RelativeBlockBatch();
    public Structure(Instance instance) {
        this.instance = instance;
    }
    protected abstract List<Integer> getRaycastsForDirection(Point center, Pos direction, int orientationBaseValue);

    public boolean canPlaceStructureAt(Point center) {
        if (placedStructure != null) return false;
        Set<Point> componentsPoints = generateComponentsPoints(center);
        for (Point point : componentsPoints) {
            if (!instance.getBlock(point).isAir() && instance.getBlock(point).isSolid()) {
                return false;
            }
        }
        return true;
    }

    public abstract void generateStructure(Point toBeGeneratedAt);

    protected void generateStructure(Point toBeGeneratedAt, Block rectanglePoint, Block cornerPoint, Block orientationPoint) {
        if (toBeGeneratedAt == null) {
            logger.error("No suitable location found to generate " + this.getClass().getSimpleName() + " at.");
            return;
        }

        //var schematic = SchematicReader.read(Path.of("schematics/frenzy_receptacle.schem"));
        //schematic.build(Rotation.NONE, null).apply(instance, toBeGeneratedAt.blockX(), toBeGeneratedAt.blockY(), toBeGeneratedAt.blockZ(),  null);


        // Get orientation of the receptacle and then execute the subsequent logic
        this.orientation = findOrientation(toBeGeneratedAt);

        Set<Point> bluePoints = new HashSet<>();
        colorOrientationFace(bluePoints, toBeGeneratedAt);

        Set<Point> rectanglePoints = generateComponentsPoints(toBeGeneratedAt);
        Set<Point> corners = new HashSet<>();
        addPillarPoints(corners, toBeGeneratedAt);

        for (Point point : rectanglePoints) {
            batch.setBlock(point, rectanglePoint);
        }
        for (Point point : corners) {
            batch.setBlock(point, cornerPoint);
        }

        for (Point bluePoint : bluePoints) {
            batch.setBlock(bluePoint, orientationPoint);
        }

        batch.apply(instance, null);
    }

    protected Set<Point> generateComponentsPoints(Point bottomCenter) {

        Set<Point> rectanglePoints = new HashSet<>();

        int halfSize = getHalfStructureSize();
        int height = getStructureHeight();

        for (int x = bottomCenter.blockX() - halfSize; x <= bottomCenter.blockX() + halfSize; x++) {
            for (int z = bottomCenter.blockZ() - halfSize; z <= bottomCenter.blockZ() + halfSize; z++) {
                for (int y = bottomCenter.blockY() + 1; y < bottomCenter.blockY() + height; y++) {
                    rectanglePoints.add(new Pos(x, y, z));
                }
            }
        }

        return rectanglePoints;
    }

    protected void colorOrientationFace(Set<Point> rectanglePoints, Point bottomCenter) {
        int halfSize = getHalfStructureSize();
        int height = getStructureHeight();

        switch (orientation) {
            case 0 -> {  // North
                for (int x = bottomCenter.blockX() - halfSize; x <= bottomCenter.blockX() + halfSize; x++) {
                    for (int y = bottomCenter.blockY() + 1; y < bottomCenter.blockY() + height; y++) {
                        rectanglePoints.add(new Pos(x, y, bottomCenter.blockZ() - halfSize));
                    }
                }
            }
            case 1 -> {  // East
                for (int z = bottomCenter.blockZ() - halfSize; z <= bottomCenter.blockZ() + halfSize; z++) {
                    for (int y = bottomCenter.blockY() + 1; y < bottomCenter.blockY() + height; y++) {
                        rectanglePoints.add(new Pos(bottomCenter.blockX() + halfSize, y, z));
                    }
                }
            }
            case 2 -> {  // South
                for (int x = bottomCenter.blockX() - halfSize; x <= bottomCenter.blockX() + halfSize; x++) {
                    for (int y = bottomCenter.blockY() + 1; y < bottomCenter.blockY() + height; y++) {
                        rectanglePoints.add(new Pos(x, y, bottomCenter.blockZ() + halfSize));
                    }
                }
            }
            case 3 -> {  // West
                for (int z = bottomCenter.blockZ() - halfSize; z <= bottomCenter.blockZ() + halfSize; z++) {
                    for (int y = bottomCenter.blockY() + 1; y < bottomCenter.blockY() + height; y++) {
                        rectanglePoints.add(new Pos(bottomCenter.blockX() - halfSize, y, z));
                    }
                }
            }
        }
    }

    protected void addPillarPoints(Set<Point> corners, Point center) {
        int halfSize = getHalfStructureSize();

        // Calculate all 4 bottom corners
        corners.add(new Pos(center.blockX() - halfSize, center.blockY(), center.blockZ() - halfSize));
        corners.add(new Pos(center.blockX() + halfSize, center.blockY(), center.blockZ() - halfSize));
        corners.add(new Pos(center.blockX() - halfSize, center.blockY(), center.blockZ() + halfSize));
        corners.add(new Pos(center.blockX() + halfSize, center.blockY(), center.blockZ() + halfSize));

        // Load chunks and then check for air blocks for pillars
        Set<Point> pillars = new HashSet<>();
        for (Point corner : corners) {
            Point belowCorner = corner.withY(corner.blockY() - 1);
            while (instance.getBlock(belowCorner).isAir()) {
                pillars.add(belowCorner);
                belowCorner = belowCorner.withY(belowCorner.blockY() - 1);
            }
        }
        corners.addAll(pillars); // Add all pillar points to the corners list after finishing the loop.
    }

    protected int findOrientation(Point toBeGeneratedAt) {

        List<Integer> averageAir = new ArrayList<>();

        // North raycasts
        averageAir.addAll(getRaycastsForDirection(toBeGeneratedAt, new Pos(0, 0, -1), 0));
        // South raycasts
        averageAir.addAll(getRaycastsForDirection(toBeGeneratedAt, new Pos(0, 0, 1), 2));
        // East raycasts
        averageAir.addAll(getRaycastsForDirection(toBeGeneratedAt, new Pos(1, 0, 0), 1));
        // West raycasts
        averageAir.addAll(getRaycastsForDirection(toBeGeneratedAt, new Pos(-1, 0, 0), 3));


        Map<Integer, Integer> orientationSums = getOrientationSums(averageAir);

        return orientationSums.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    @NotNull
    static Map<Integer, Integer> getOrientationSums(List<Integer> averageAir) {
        Map<Integer, Integer> orientationSums = new HashMap<>();
        for (Integer average : averageAir) {
            int orientation = average / 100; // This assumes values like 100, 101, 102 represent North and 200, 201, 202 represent South, etc.
            int count = average % 100; // Gets the actual count
            orientationSums.put(orientation, orientationSums.getOrDefault(orientation, 0) + count);
        }

        return orientationSums;
    }

    RaycastResult raycastAirBlocks(Point startingPoint, Point direction, int maxLength, Instance instance) {

        int count = 0;
        Point currentPoint = startingPoint;
        List<Point> pointsToCheck = new ArrayList<>();

        for (int i = 0; i < maxLength; i++) {
            if (Math.abs(currentPoint.x()) >= CaveGenerator.caveSizeX
                    || Math.abs(currentPoint.z()) >= CaveGenerator.caveSizeZ) {
                break;
            }
            pointsToCheck.add(currentPoint);
            currentPoint = currentPoint.add(direction);
        }

        boolean hitWall = false;  // To track if we've hit a wall

        Point lastPoint = pointsToCheck.get(pointsToCheck.size() - 1);
        for (Point point : pointsToCheck) {
            if (hitWall) {
                break; // If we've hit a wall previously, stop processing the remaining points
            }

            if (instance.getBlock(point).isAir()
                    || !instance.getBlock(point).isSolid()
                    || instance.getBlock(point).hasTag(Identifier.getGlobalTag())) {
                if (instance.getBlock(point).hasTag(Identifier.getGlobalTag())) {
                    if (!new Identifier(instance.getBlock(point).getTag(Identifier.getGlobalTag())).equals(CloudBlock.identifier)) {
                        continue;
                    }
                }
                count++;
                //batch.setBlock(point, Block.PURPLE_WOOL); // Setting the block to purple wool
                lastPoint = point;
            } else {
                hitWall = true; // Set the flag to true so that we stop processing the following points
            }
        }

        return new RaycastResult(count, lastPoint);
    }
}
