package gui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;


public class OpenLauncherGui {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static ArrayList<RenderComponet> componets = new ArrayList<RenderComponet>();
    public boolean isRunning = true;

    public OpenLauncherGui() {
        setUpDisplay();
        setUpOpenGL();
        init();

        while (isRunning) {
            render();
            input();
            Display.update();
            Display.sync(60);
            if (Display.isCloseRequested()) {
                isRunning = false;
            }
        }
        Display.destroy();
    }

    public static void main(String[] args) {
        new OpenLauncherGui();
    }

    public void input() {

    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        glRecti(10, 10, 20, 20);
        for (RenderComponet renderComponet : componets) {
            renderComponet.render();
        }
    }

    public void setUpOpenGL() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);

    }

    public void init() {
        componets.add(new Block());
    }

    public void setUpDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle("OpenLauncher");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}
