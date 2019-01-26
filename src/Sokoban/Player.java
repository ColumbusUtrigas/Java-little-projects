package Sokoban;

class Player
{
    int x, y;
    boolean visible;

    Player(int x, int y, boolean visible)
    {
        this.x = x;
        this.y = y;
        this.visible = visible;
    }

    void move(int x, int y, Map map)
    {
        if (map.blocks[this.y + y][this.x + x].id == 1) return;
        if (!map.blocks[this.y + y][this.x + x].move(x, y))  return;

        this.x += x;
        this.y += y;
    }

    void update()
    {

    }
}
