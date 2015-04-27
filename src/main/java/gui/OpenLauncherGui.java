package gui;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glRecti;
import static org.lwjgl.opengl.GL11.glViewport;


public class OpenLauncherGui {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static ArrayList<RenderComponet> componets = new ArrayList<RenderComponet>();
    public boolean isRunning = true;
    private long lastFrame;


    public PacksComponent packsComponent = new PacksComponent();
    public static LoadingComponet loadingComponet = new LoadingComponet();

    public void start(){
        setUpDisplay();
        setUpOpenGL();
        init();
        setUpTimer();

        while (isRunning) {
            render();
            logic(getDelta());
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
        for (RenderComponet renderComponet : componets) {
            renderComponet.input();
        }
    }

    private long getTime() {
        return (Sys.getTime() * 1000 / Sys.getTimerResolution());
    }

    private void setUpTimer() {
        lastFrame = getTime();
    }

    private int getDelta() {
        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }

    private void logic(int delta) {
        for (RenderComponet componet : componets) {
            componet.logic(delta);
        }
    }


    public void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        glRecti(10, 10, 20, 20);
        for (RenderComponet renderComponet : componets) {
            GL11.glPushMatrix();
            renderComponet.render();
            GL11.glPopMatrix();
        }
    }

    public void setUpOpenGL() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);


        glEnable(GL_TEXTURE_2D);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // enable alpha blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glViewport(0, 0, WIDTH, HEIGHT);
        glMatrixMode(GL_MODELVIEW);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    public void init() {
        componets.add(new Wallpaper());
        componets.add(new Footer());
        packsComponent.setupTextures();
        componets.add(packsComponent);
        loadingComponet.setupTextures();
        componets.add(loadingComponet);
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
