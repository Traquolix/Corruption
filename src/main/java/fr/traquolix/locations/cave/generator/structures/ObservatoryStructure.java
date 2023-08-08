package fr.traquolix.locations.cave.generator.structures;

import fr.traquolix.locations.cave.generator.CaveGenerator;
import fr.traquolix.locations.cave.generator.structures.helpers.RaycastResult;
import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class ObservatoryStructure extends Structure {

    final int halfStructureSize = 9;
    final int structureHeight = 20;
    Point telescopePointingAt = null;
    public ObservatoryStructure(Instance instance) {
        super(instance);
    }

    public void generateStructure(Point toBeGeneratedAt) {
        super.generateStructure(toBeGeneratedAt, Block.COPPER_BLOCK, Block.OAK_FENCE, Block.OXIDIZED_COPPER);
    }

    @Override
    protected int findOrientation(Point toBeGeneratedAt) {

        Random random = new Random();
        int direction = random.nextInt(0, 4);

        if (toBeGeneratedAt.x() + 100 >= CaveGenerator.caveSizeX) {
            direction = 3;
        } else if (toBeGeneratedAt.x() - 100 <= -CaveGenerator.caveSizeX) {
            direction = 1;
        } else if (toBeGeneratedAt.z() + 100 >= CaveGenerator.caveSizeZ) {
            direction = 0;
        } else if (toBeGeneratedAt.z() - 100 <= -CaveGenerator.caveSizeZ) {
            direction = 2;
        }

        switch (direction) {
            case 0 ->  // North
                    getRaycastsForDirection(toBeGeneratedAt, new Pos(0, 1, -1), direction);
            case 1 ->  // East
                    getRaycastsForDirection(toBeGeneratedAt, new Pos(1, 1, 0), direction);
            case 2 ->  // South
                    getRaycastsForDirection(toBeGeneratedAt, new Pos(0, 1, 1), direction);
            case 3 ->  // West
                    getRaycastsForDirection(toBeGeneratedAt, new Pos(-1, 1, 0), direction);
        }

        return direction;
    }

    @Override
    protected List<Integer> getRaycastsForDirection(Point center, Pos direction, int orientationBaseValue) {
        List<Point> points = switch (orientationBaseValue) {
            case 0 ->  // North
                    List.of(
                            center.add(0, structureHeight, -halfStructureSize -1)
                    );
            case 1 ->  // East
                    List.of(
                            center.add(halfStructureSize +1, structureHeight, 0)
                    );
            case 2 ->  // South
                    List.of(
                            center.add(0, structureHeight, halfStructureSize +1)
                    );
            case 3 ->  // West
                    List.of(
                            center.add(-halfStructureSize -1, structureHeight, 0)
                    );
            default -> new ArrayList<>(); // or throw an error
        };


        List<Integer> averageAir = new ArrayList<>();

        RaycastResult raycastResult = null;
        for (Point point : points) {
            raycastResult = raycastAirBlocks(point, direction, 50, instance);
            averageAir.add(orientationBaseValue * 100 + raycastResult.count());
        }

        assert raycastResult != null;
        telescopePointingAt = raycastResult.lastAirPosition();

        return averageAir;
    }
}