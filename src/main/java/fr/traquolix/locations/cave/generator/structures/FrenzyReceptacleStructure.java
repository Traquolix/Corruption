package fr.traquolix.locations.cave.generator.structures;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FrenzyReceptacleStructure extends Structure {

    final int halfStructureSize = 5;
    @Getter
    final int structureHeight = 9;

    public FrenzyReceptacleStructure(Instance instance) {
        super(instance);
    }

    public void generateStructure(Point toBeGeneratedAt) {
        super.generateStructure(toBeGeneratedAt, Block.QUARTZ_BLOCK, Block.QUARTZ_PILLAR, Block.BLUE_WOOL);
    }

    @Override
    protected List<Integer> getRaycastsForDirection(Point center, Pos direction, int orientationBaseValue) {
        List<Point> points = switch (orientationBaseValue) {
            case 0 ->  // North
                    List.of(
                            center.add(-halfStructureSize, structureHeight / 2f, -halfStructureSize -1),
                            center.add(0, structureHeight / 2f, -halfStructureSize -1),
                            center.add(halfStructureSize, structureHeight / 2f, -halfStructureSize -1),

                            center.add(-halfStructureSize, 1, -halfStructureSize -1),
                            center.add(0, 1, -halfStructureSize -1),
                            center.add(halfStructureSize, 1, -halfStructureSize -1),

                            center.add(-halfStructureSize, structureHeight -1, -halfStructureSize -1),
                            center.add(0, structureHeight -1, -halfStructureSize -1),
                            center.add(halfStructureSize, structureHeight -1, -halfStructureSize -1)
                    );
            case 1 ->  // East
                    List.of(
                            center.add(halfStructureSize +1, structureHeight / 2f, -halfStructureSize),
                            center.add(halfStructureSize +1, structureHeight / 2f, 0),
                            center.add(halfStructureSize +1, structureHeight / 2f, halfStructureSize),

                            center.add(halfStructureSize +1, 1, -halfStructureSize),
                            center.add(halfStructureSize +1, 1, 0),
                            center.add(halfStructureSize +1, 1, halfStructureSize),

                            center.add(halfStructureSize +1, structureHeight -1, -halfStructureSize),
                            center.add(halfStructureSize +1, structureHeight -1, 0),
                            center.add(halfStructureSize +1, structureHeight -1, halfStructureSize)
                    );
            case 2 ->  // South
                    List.of(
                            center.add(-halfStructureSize, structureHeight / 2f, halfStructureSize +1),
                            center.add(0, structureHeight / 2f, halfStructureSize +1),
                            center.add(halfStructureSize, structureHeight / 2f, halfStructureSize +1),

                            center.add(-halfStructureSize, 1, halfStructureSize +1),
                            center.add(0, 1, halfStructureSize +1),
                            center.add(halfStructureSize, 1, halfStructureSize +1),

                            center.add(-halfStructureSize, structureHeight -1, halfStructureSize +1),
                            center.add(0, structureHeight -1, halfStructureSize +1),
                            center.add(halfStructureSize, structureHeight -1, halfStructureSize +1)
                    );
            case 3 ->  // West
                    List.of(
                            center.add(-halfStructureSize -1, structureHeight / 2f, -halfStructureSize),
                            center.add(-halfStructureSize -1, structureHeight / 2f, 0),
                            center.add(-halfStructureSize -1, structureHeight / 2f, halfStructureSize),

                            center.add(-halfStructureSize -1, 1, -halfStructureSize),
                            center.add(-halfStructureSize -1, 1, 0),
                            center.add(-halfStructureSize -1, 1, halfStructureSize),

                            center.add(-halfStructureSize -1, structureHeight -1, -halfStructureSize),
                            center.add(-halfStructureSize -1, structureHeight -1, 0),
                            center.add(-halfStructureSize -1, structureHeight -1, halfStructureSize)
                    );
            default -> new ArrayList<>(); // or throw an error
        };


        List<Integer> averageAir = new ArrayList<>();

        for (Point point : points) {
            averageAir.add(orientationBaseValue * 100 + raycastAirBlocks(point, direction, 50, instance).count());
        }

        return averageAir;
    }
}
