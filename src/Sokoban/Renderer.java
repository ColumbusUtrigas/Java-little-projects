package Sokoban;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Renderer extends JPanel
{
    private static Color[] ColorMap =
    {
            Color.BLACK,
            Color.GRAY,
            Color.GREEN,
            Color.BLUE,
            Color.CYAN
    };

    private Map map;
    private Player player;
    private BufferedImage[] ImageMap = new BufferedImage[5];

    Renderer(Map map, Player player)
    {
        this.map = map;
        this.player = player;

        try {
            ImageMap[1] = ImageIO.read(new File("brick.png"));
            ImageMap[2] = ImageIO.read(new File("box.png"));
            ImageMap[3] = ImageIO.read(new File("key.png"));
            ImageMap[4] = ImageIO.read(new File("opened_box.png"));
        } catch (IOException e) {
            System.out.println();
        }
    }

    @Override
    public void paint(Graphics g)
    {
        int width = this.getWidth();
        int height = this.getHeight();
        int w = width / Map.width;
        int h = height / Map.height;

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (ImageMap[map.blocks[i][j].id] != null) {
                    g.drawImage(ImageMap[map.blocks[i][j].id], w * j, h * i, w, h, new Color(0, 0, 0), null);
                } else {
                    g.setColor(ColorMap[map.blocks[i][j].id]);
                    g.fillRect(w * j, h * i, w, h);
                }
            }
        }

        if (player != null) {
            if (player.visible) {
                g.setColor(Color.RED);
                g.fillRect(player.x * w, player.y * h, w, h);
            }
        }
    }
}
