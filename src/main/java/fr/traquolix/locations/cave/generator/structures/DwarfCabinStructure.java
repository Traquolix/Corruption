package fr.traquolix.locations.cave.generator.structures;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DwarfCabinStructure extends Structure {

    @Getter
    final int halfStructureSize = 9;
    @Getter
    final int structureHeight = 20;

    public DwarfCabinStructure(Instance instance) {
        super(instance);
    }

    public void generateStructure(Point toBeGeneratedAt) {
        super.generateStructure(toBeGeneratedAt, Block.SPRUCE_PLANKS, Block.SPRUCE_FENCE, Block.BLUE_WOOL);
    }

    @Override
    protected List<Integer> getRaycastsForDirection(Point center, Pos direction, int orientationBaseValue) {
        List<Point> points = switch (orientationBaseValue) {
            case 0 ->  // North
                    List.of(
                            center.add(-getHalfStructureSize(), getStructureHeight() / 2f, -getHalfStructureSize()-1),
                            center.add(0, getStructureHeight() / 2f, -getHalfStructureSize()-1),
                            center.add(getHalfStructureSize(), getStructureHeight() / 2f, -getHalfStructureSize()-1),

                            center.add(-getHalfStructureSize(), 1, -getHalfStructureSize()-1),
                            center.add(0, 1, -getHalfStructureSize()-1),
                            center.add(getHalfStructureSize(), 1, -getHalfStructureSize()-1),

                            center.add(-getHalfStructureSize(), getStructureHeight()-1, -getHalfStructureSize()-1),
                            center.add(0, getStructureHeight()-1, -getHalfStructureSize()-1),
                            center.add(getHalfStructureSize(), getStructureHeight()-1, -getHalfStructureSize()-1)
                    );
            case 1 ->  // East
                    List.of(
                            center.add(getHalfStructureSize()+1, getStructureHeight() / 2f, -getHalfStructureSize()),
                            center.add(getHalfStructureSize()+1, getStructureHeight() / 2f, 0),
                            center.add(getHalfStructureSize()+1, getStructureHeight() / 2f, getHalfStructureSize()),

                            center.add(getHalfStructureSize()+1, 1, -getHalfStructureSize()),
                            center.add(getHalfStructureSize()+1, 1, 0),
                            center.add(getHalfStructureSize()+1, 1, getHalfStructureSize()),

                            center.add(getHalfStructureSize()+1, getStructureHeight()-1, -getHalfStructureSize()),
                            center.add(getHalfStructureSize()+1, getStructureHeight()-1, 0),
                            center.add(getHalfStructureSize()+1, getStructureHeight()-1, getHalfStructureSize())
                    );
            case 2 ->  // South
                    List.of(
                            center.add(-getHalfStructureSize(), getStructureHeight() / 2f, getHalfStructureSize()+1),
                            center.add(0, getStructureHeight() / 2f, getHalfStructureSize()+1),
                            center.add(getHalfStructureSize(), getStructureHeight() / 2f, getHalfStructureSize()+1),

                            center.add(-getHalfStructureSize(), 1, getHalfStructureSize()+1),
                            center.add(0, 1, getHalfStructureSize()+1),
                            center.add(getHalfStructureSize(), 1, getHalfStructureSize()+1),

                            center.add(-getHalfStructureSize(), getStructureHeight()-1, getHalfStructureSize()+1),
                            center.add(0, getStructureHeight()-1, getHalfStructureSize()+1),
                            center.add(getHalfStructureSize(), getStructureHeight()-1, getHalfStructureSize()+1)
                    );
            case 3 ->  // West
                    List.of(
                            center.add(-getHalfStructureSize()-1, getStructureHeight() / 2f, -getHalfStructureSize()),
                            center.add(-getHalfStructureSize()-1, getStructureHeight() / 2f, 0),
                            center.add(-getHalfStructureSize()-1, getStructureHeight() / 2f, getHalfStructureSize()),

                            center.add(-getHalfStructureSize()-1, 1, -getHalfStructureSize()),
                            center.add(-getHalfStructureSize()-1, 1, 0),
                            center.add(-getHalfStructureSize()-1, 1, getHalfStructureSize()),

                            center.add(-getHalfStructureSize()-1, getStructureHeight()-1, -getHalfStructureSize()),
                            center.add(-getHalfStructureSize()-1, getStructureHeight()-1, 0),
                            center.add(-getHalfStructureSize()-1, getStructureHeight()-1, getHalfStructureSize())
                    );
            default -> new ArrayList<>(); // or throw an error
        };

        List<Integer> averageAir = new ArrayList<>();

        for (Point point : points) {
            averageAir.add(orientationBaseValue * 100 + raycastAirBlocks(point, direction, 50, getInstance()).count());
        }

        return averageAir;
    }
}