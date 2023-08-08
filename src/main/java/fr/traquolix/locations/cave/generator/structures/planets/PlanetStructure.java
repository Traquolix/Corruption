package fr.traquolix.locations.cave.generator.structures.planets;

import fr.traquolix.locations.cave.generator.CaveGenerator;
import fr.traquolix.locations.cave.generator.structures.Structure;
import fr.traquolix.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.*;

import static fr.traquolix.Main.logger;
@Getter @Setter
public class PlanetStructure extends Structure {

    double radiusX;
    double radiusY;
    double radiusZ;
    Block block = Block.STONE;

    public PlanetStructure(Instance instance, double radiusX, double radiusY, double radiusZ) {
        super(instance);
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
    }

    @Override
    protected int findOrientation(Point toBeGeneratedAt) {
        Random random = new Random();
        return random.nextInt(0,4);
    }

    public boolean canPlaceStructureAt(Point center) {
        return true;
    }

    @Override
    protected List<Integer> getRaycastsForDirection(Point center, Pos direction, int orientationBaseValue) {
        return null;
    }

    @Override
    public void generateStructure(Point toBeGeneratedAt) {
        if (toBeGeneratedAt == null) {
            logger.error("No suitable location found to generate " + this.getClass().getSimpleName() + " at.");
            return;
        }

        Set<Point> rectanglePoints = generateComponentsPoints(toBeGeneratedAt);


        for (Point point : rectanglePoints) {
            batch.setBlock(point, getBlock());
        }

        batch.apply(this.getInstance(), null);
    }

    @Override
    protected Set<Point> generateComponentsPoints(Point center) {
        Set<Point> points = new HashSet<>();

        setRadiusX(getRadiusX()+0.5);
        setRadiusY(getRadiusY()+0.5);
        setRadiusZ(getRadiusZ()+0.5);

        final double invRadiusX = 1 / getRadiusX();
        final double invRadiusY = 1 / getRadiusY();
        final double invRadiusZ = 1 / getRadiusZ();

        final int ceilRadiusX = (int) Math.ceil(getRadiusX());
        final int ceilRadiusY = (int) Math.ceil(getRadiusY());
        final int ceilRadiusZ = (int) Math.ceil(getRadiusZ());

        double nextXn = 0;
        forX: for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0;
            forY: for (int y = 0; y <= ceilRadiusY; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0;
                forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;

                    double distanceSq = Utils.lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }

                    Pos pos = new Pos(center.blockX(), center.blockY(), center.blockZ());

                    Pos[] positions = {
                            pos.add(x, y, z),
                            pos.add(x, y, -z),
                            pos.add(x, -y, z),
                            pos.add(x, -y, -z),
                            pos.add(-x, y, z),
                            pos.add(-x, y, -z),
                            pos.add(-x, -y, z),
                            pos.add(-x, -y, -z)
                    };

                    // Set the blocks in the block batch
                    points.addAll(Arrays.asList(positions));

                }
            }
        }
        return points;
    }


    boolean isOnEdge(int x, int y, int z, double rx, double ry, double rz) {
        double valueAtXYZ = (x*x)/(rx*rx) + (y*y)/(ry*ry) + (z*z)/(rz*rz);
        if (valueAtXYZ > 1) return false; // This shouldn't happen but just in case

        // Check neighbors
        if ((x+1)*(x+1)/(rx*rx) + y*y/(ry*ry) + z*z/(rz*rz) > 1) return true;
        if ((x-1)*(x-1)/(rx*rx) + y*y/(ry*ry) + z*z/(rz*rz) > 1) return true;
        if (x*x/(rx*rx) + (y+1)*(y+1)/(ry*ry) + z*z/(rz*rz) > 1) return true;
        if (x*x/(rx*rx) + (y-1)*(y-1)/(ry*ry) + z*z/(rz*rz) > 1) return true;
        if (x*x/(rx*rx) + y*y/(ry*ry) + (z+1)*(z+1)/(rz*rz) > 1) return true;
        if (x*x/(rx*rx) + y*y/(ry*ry) + (z-1)*(z-1)/(rz*rz) > 1) return true;

        return false; // If none of the neighbors are outside, it's not an edge block
    }
}
