package fr.traquolix.content.items.helpers;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.map.framebuffers.Graphics2DFramebuffer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RotatingMapRenderer {

    private final int SIZE = 128;  // assuming a 128x128 map
    @Getter
    private final Graphics2DFramebuffer framebuffer;

    public RotatingMapRenderer() {
        this.framebuffer = new Graphics2DFramebuffer();
    }

    public BufferedImage captureWorldSlice(Instance world, Pos playerPosition ) {
        BufferedImage slice = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = slice.createGraphics();

        // Here, you'd fill the BufferedImage with colors representing the blocks in a slice of the world around the player.
        // This is a stub example; you'd replace this with actual world data capture.
        g.setColor(Color.GREEN);  // example grass color
        g.fillRect(0, 0, SIZE, SIZE);

        return slice;
    }

    public BufferedImage rotateSlice(BufferedImage slice, double yaw) {
        BufferedImage rotated = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = rotated.createGraphics();

        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(yaw), SIZE / 2, SIZE / 2);
        g.setTransform(transform);
        g.drawImage(slice, 0, 0, null);

        return rotated;
    }

    public void renderToMap(Player player, Pos playerPosition) {
        BufferedImage slice = captureWorldSlice(player.getInstance(), playerPosition);
        BufferedImage rotatedSlice = rotateSlice(slice, playerPosition.yaw());  // Use actual yaw value here

        Graphics2D renderer = framebuffer.getRenderer();
        renderer.drawImage(rotatedSlice, 0, 0, null);

        // Now, you can send this framebuffer data to the player's map item.
        // Use your packet sending mechanism to do this.
    }
}

