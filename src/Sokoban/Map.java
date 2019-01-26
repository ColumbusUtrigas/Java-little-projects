package Sokoban;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

class Map
{
    class Block
    {
        int id = 0;
        int x = 0, y = 0;

        boolean move(int x, int y)
        {
            if (this.id == 0 || this.id == 3) return true;
            Block next = blocks[this.y + y][this.x + x];

            if (this.id == 2) {
                if (next.id == 0) {
                    next.id = this.id;
                    this.id = 0;
                    return true;
                }

                if (next.id == 3) {
                    next.id = 4;
                    this.id = 0;
                    return true;
                }
            }

            if (this.id == 4) {
                if (next.id == 0) {
                    next.id = 2;
                    this.id = 3;
                    return true;
                }
            }

            return false;
        }
    }

    static final int width = 20, height = 20;
    Block[][] blocks = new Block[width][height];
    int playerx = 0, playery = 0;

    boolean finished()
    {
        for (Block[] row : blocks)
            for (Block block : row)
                if (block.id == 2 || block.id == 3)
                    return false;

        return true;
    }

    Map()
    {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                blocks[i][j] = new Block();
    }

    Map(String filename)
    {
        this();

        if (!load(filename))
        {
            JOptionPane.showMessageDialog(null, "Cannot load map", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    boolean load(String filename)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            int row = 0;
            String line = reader.readLine();
            playerx = Integer.parseInt(line.substring(0, line.indexOf(' ')));
            playery = Integer.parseInt(line.substring(line.indexOf(' ') + 1));

            do
            {
                line = reader.readLine();
                int pos = 0;

                for (char C : line.toCharArray())
                {
                    if (C >= '0' && C <= '9' && pos < width)
                    {
                        blocks[row][pos].id = C - '0';
                        blocks[row][pos].x = pos;
                        blocks[row][pos].y = row;
                        pos++;
                    }
                }

                row++;
            } while (row < height);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    boolean save(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(playerx + " " + playery + "\n");

            for (Block[] row : blocks) {
                for (Block block : row) {
                    writer.write(block.id + " ");
                }

                writer.write("\n");
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
