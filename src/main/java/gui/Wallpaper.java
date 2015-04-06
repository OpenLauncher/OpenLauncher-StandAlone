package gui;


import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;


public class Wallpaper implements RenderComponet {

    Texture texture;

    public Wallpaper() {
        try {
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/back" + OpenLauncherGui.WIDTH + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        Color.white.bind();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(0 + texture.getTextureWidth(), 0);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(0 + texture.getTextureWidth(), 0 + texture.getTextureHeight());
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(0, 0 + texture.getTextureHeight());
        GL11.glEnd();
    }

    @Override
    public void input() {

    }

    @Override
    public void logic(int delta) {

    }
}
