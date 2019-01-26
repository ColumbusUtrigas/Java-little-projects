package Sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Editor implements ActionListener
{
    private Map map;
    private Player player;
    private Renderer renderer;
    private Input input = new Input();
    private int mode = 0;
    private String currentFile;

    public Editor()
    {
        JFrame window = new JFrame("Sokoban Editor");
        map = new Map();
        player = new Player(0, 0, false);
        renderer = new Renderer(map, player);

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveAs = new JMenuItem("Save as");
        JMenuItem exit = new JMenuItem("Exit");

        menu.add(file);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(exit);

        open.addActionListener(e -> {
            currentFile = null;
            JFileChooser fileopen = new JFileChooser("./");
            if (fileopen.showDialog(window, "Open") == JFileChooser.APPROVE_OPTION) {
                if (!map.load(fileopen.getSelectedFile().getName())) {
                    JOptionPane.showMessageDialog(window, "Cannot load map", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                player.x = map.playerx;
                player.y = map.playery;
                player.visible = true;
                currentFile = fileopen.getSelectedFile().getName();
            }
        });

        save.addActionListener(e -> {
            if (currentFile == null) {
                JFileChooser filesave = new JFileChooser("./");
                if (filesave.showDialog(window, "Open") != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                currentFile = filesave.getSelectedFile().getName();
            }

            save(currentFile);
        });

        saveAs.addActionListener(e -> {
            JFileChooser filesave = new JFileChooser("./");
            if (filesave.showDialog(window, "Open") == JFileChooser.APPROVE_OPTION) {
                save(filesave.getSelectedFile().getName());
            }
        });

        exit.addActionListener(e -> System.exit(0));

        window.setJMenuBar(menu);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        JPanel toolbar = new JPanel(new GridLayout(5, 1));
        JToggleButton space = new JToggleButton("Space", true);
        JToggleButton wall = new JToggleButton("Wall");
        JToggleButton box = new JToggleButton("Box");
        JToggleButton key = new JToggleButton("Key");
        JToggleButton player = new JToggleButton("Player");

        space.addActionListener(e -> mode = 0);
        wall.addActionListener(e -> mode = 1);
        box.addActionListener(e -> mode = 2);
        key.addActionListener(e -> mode = 3);
        player.addActionListener(e -> mode = 4);

        ButtonGroup group = new ButtonGroup();
        group.add(space);  toolbar.add(space);
        group.add(wall);   toolbar.add(wall);
        group.add(box);    toolbar.add(box);
        group.add(key);    toolbar.add(key);
        group.add(player); toolbar.add(player);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;

        constraints.weightx = 0.1;
        mainPanel.add(toolbar, constraints);

        constraints.weightx = 0.9;
        mainPanel.add(renderer, constraints);

        window.add(mainPanel);
        window.addKeyListener(input);
        window.setFocusable(true);
        space.addKeyListener(input);
        wall.addKeyListener(input);
        box.addKeyListener(input);
        key.addKeyListener(input);
        player.addKeyListener(input);
        toolbar.addKeyListener(input);
        renderer.addKeyListener(input);
        renderer.addMouseListener(input);
        renderer.addMouseMotionListener(input);
        renderer.setPreferredSize(new Dimension(640, 640));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);
        window.pack();

        Timer timer = new Timer(16, this);
        timer.start();
    }

    private void update()
    {
        int w = renderer.getWidth() / 20;
        int h = renderer.getHeight() / 20;

        if (input.isKeyDown(KeyEvent.VK_DELETE) || input.isKeyDown(KeyEvent.VK_BACK_SPACE)) {
            for (Map.Block[] row : map.blocks) {
                for (Map.Block block : row) {
                    block.id = 0;
                }
            }

            player.visible = false;
        }

        if (input.getMouse().buttons[MouseEvent.BUTTON1]) {
            int row = input.getMouse().y / h;
            int col = input.getMouse().x / w;

            if (row >= 0 && row <= 19 && col >= 0 && col <= 19) {
                if (mode >= 0 && mode <= 3) {
                    if (player.x == col && player.y == row) {
                        player.visible = false;
                    }

                    map.blocks[row][col].id = mode;
                } else if (mode == 4) {
                    map.blocks[row][col].id = 0;
                    player.x = map.playerx = col;
                    player.y = map.playery = row;
                    player.visible = true;
                }
            }
        }

        input.update();
        renderer.repaint();
    }

    private void save(String filename) {
        int boxes = 0, keys = 0;
        for (Map.Block[] row : map.blocks) {
            for (Map.Block block : row) {
                boxes += block.id == 2 ? 1 : 0;
                keys  += block.id == 3 ? 1 : 0;
            }
        }

        if (boxes == 0 || keys == 0) {
            JOptionPane.showMessageDialog(null, "No boxes or keys on the map", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (boxes != keys) {
            JOptionPane.showMessageDialog(null, "Number of boxes not equals to number of keys", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!player.visible) {
            JOptionPane.showMessageDialog(null, "No player on the map", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!map.save(filename)) {
            JOptionPane.showMessageDialog(null, "Cannot save map", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentFile = filename;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        update();
    }
}
