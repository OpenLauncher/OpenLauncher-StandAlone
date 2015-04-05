package gui;

import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glRecti;

public class Block implements RenderComponet {

    @Override
    public void render() {
        glColor3d(255, 0, 0);
        glRecti(Mouse.getX(), OpenLauncherGui.HEIGHT - Mouse.getY(), Mouse.getX() + 50, OpenLauncherGui.HEIGHT - Mouse.getY() + 50);
        glColor3d(255, 255, 255);
    }
}
