package Sokoban;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Game implements ActionListener
{
    private JFrame window;
    private Input input = new Input();
    private Map map;
    private Player player;
    private Renderer renderer;

    public Game()
    {
        window = new JFrame("Sokoban");

        JFileChooser fileopen = new JFileChooser("./");
        fileopen.setFileFilter(new FileNameExtensionFilter("Map file", "map"));
        if (fileopen.showDialog(window, "Open") != JFileChooser.APPROVE_OPTION) {
            System.exit(1);
        }

        map = new Map(fileopen.getSelectedFile().getName());
        player = new Player(map.playerx, map.playery, true);
        renderer = new Renderer(map, player);

        window.add(renderer);
        window.addKeyListener(input);
        window.getContentPane().setPreferredSize(new Dimension(640, 640));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);
        window.pack();

        Timer timer = new Timer(16, this);
        timer.start();
    }

    private void update()
    {
        if (map.finished()) {
            JOptionPane.showMessageDialog(window,
                    "Level finished",
                    "Congratulations",
                    JOptionPane.INFORMATION_MESSAGE);

            System.exit(0);
        }

        if (input.isKeyDown(KeyEvent.VK_A)) player.move(-1, 0, map);
        if (input.isKeyDown(KeyEvent.VK_D)) player.move(1, 0, map);
        if (input.isKeyDown(KeyEvent.VK_W)) player.move(0, -1, map);
        if (input.isKeyDown(KeyEvent.VK_S)) player.move(0, 1, map);

        input.update();
        player.update();
        renderer.repaint();
    }

    public void actionPerformed(ActionEvent e)
    {
        update();
    }
}
