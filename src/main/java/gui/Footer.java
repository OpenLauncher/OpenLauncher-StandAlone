package gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRecti;


public class Footer implements RenderComponet {

    public static boolean isOpen = false;
    public int movementPercentage = 0;
    public boolean isMoving = false;
    public boolean opening = false;
    Texture settingTEX;
    TrueTypeFont font;

    public Footer() {
        try {
            settingTEX = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/wrench.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font awtFont = new Font("Latha", Font.BOLD, 24);
        font = new TrueTypeFont(awtFont, false);
    }

    @Override
    public void render() {
        if (isMoving) {
            if (opening) {
                if (movementPercentage <= 1000) {
                    movementPercentage += 70;
                }
                if (movementPercentage >= 1000) {
                    isMoving = false;
                    isOpen = true;
                    movementPercentage = 1000;
                }
            } else {
                if (movementPercentage >= 0) {
                    movementPercentage -= 70;
                }
                if (movementPercentage <= 0) {
                    isMoving = false;
                    isOpen = false;
                    movementPercentage = 0;
                }
            }
        }
        if (isMoving || isOpen) {
            Color color = new Color(52, 152, 219);
            glPushMatrix();
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glColor4f(color.r, color.g, color.b, (float) ((float) movementPercentage / (float) 1000) + 0.1F);
            glRecti(0, OpenLauncherGui.HEIGHT - 40 - (movementPercentage / 10), OpenLauncherGui.WIDTH, OpenLauncherGui.HEIGHT);
            glDisable(GL_BLEND);
            glPopMatrix();

            if (movementPercentage == 1000) {
                //The wrench icon.
                glPushMatrix();
                glEnable(GL_BLEND);
                glEnable(GL_TEXTURE_2D);
                Color.white.bind();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, settingTEX.getTextureID());
                GL11.glBegin(GL11.GL_QUADS);
                int x = 10;
                int y = OpenLauncherGui.HEIGHT - 130;
                GL11.glTexCoord2f(0, 0);
                GL11.glVertex2f(x, y);
                GL11.glTexCoord2f(1, 0);
                GL11.glVertex2f(x + settingTEX.getTextureWidth(), y);
                GL11.glTexCoord2f(1, 1);
                GL11.glVertex2f(x + settingTEX.getTextureWidth(), y + settingTEX.getTextureHeight());
                GL11.glTexCoord2f(0, 1);
                GL11.glVertex2f(x, y + settingTEX.getTextureHeight());
                GL11.glEnd();
                glDisable(GL_BLEND);
                glPopMatrix();

                glPushMatrix();
                //TODO draw the textures for the bottom bar
                glEnable(GL_BLEND);
                Color.white.bind();
                font.drawString(55, OpenLauncherGui.HEIGHT - 130, "OpenLauncher Settings:", Color.white);
                glDisable(GL_BLEND);
                glPopMatrix();
            }
        } else {
            //Closed

            Color color = new Color(41, 128, 185);
            glPushMatrix();
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glColor4f(color.r, color.g, color.b, 0.5F);
            glRecti(0, OpenLauncherGui.HEIGHT - 40, OpenLauncherGui.WIDTH, OpenLauncherGui.HEIGHT);
            glDisable(GL_BLEND);
            glPopMatrix();


            glPushMatrix();
            //TODO draw the textures for the bottom bar
            glEnable(GL_BLEND);
            Color.white.bind();
            font.drawString(OpenLauncherGui.WIDTH / 2 - 90, OpenLauncherGui.HEIGHT - 30, "^SETTINGS^", Color.white);
            glDisable(GL_BLEND);
            glPopMatrix();
        }
    }

    @Override
    public void input() {
        if (!isOpen) {
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                isOpen = true;
                opening = true;
                movementPercentage = 0;
                isMoving = true;
            }
        } else {
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                opening = false;
                movementPercentage = 1000;
                isMoving = true;
                isOpen = false;
            }
        }
    }

    @Override
    public void logic(int delta) {

    }
}
