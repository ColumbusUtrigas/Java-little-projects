package Sokoban;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

class Input implements KeyListener, MouseListener, MouseMotionListener
{
    class Mouse {
        int x, y;
        boolean[] buttons = new boolean[4];
        Component component;
    }

    private static final int keysNumber = 256;
    private boolean[] keysLast = new boolean[keysNumber];
    private boolean[] keys = new boolean[keysNumber];
    private Mouse mouse = new Mouse();

    Input() {
        Arrays.fill(keysLast, false);
        Arrays.fill(keys, false);
    }

    void update() {
        System.arraycopy(keys, 0, keysLast, 0, keysNumber);
    }

    boolean isKeyDown(int keycode) { return keys[keycode] && !keysLast[keycode]; }
    Mouse getMouse() { return mouse; }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mouse.buttons[e.getButton()] = true;
        mouse.component = e.getComponent();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouse.buttons[e.getButton()] = false;
        mouse.component = e.getComponent();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        mouse.x = e.getX();
        mouse.y = e.getY();
        mouse.component = e.getComponent();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse.x = e.getX();
        mouse.y = e.getY();
        mouse.component = e.getComponent();
    }
}
