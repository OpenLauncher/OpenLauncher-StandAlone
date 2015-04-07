package gui;

import openlauncher.Launch;
import openlauncher.ModPack;
import openlauncher.ModPackInstaller;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class PacksComponent implements RenderComponet {
    Texture panelBackGroundTEX;
    Icon play;
    public ArrayList<ModPack> packs = new ArrayList<ModPack>();
    int scroll;
    int scrolltwo = 0;
    boolean isScrolling = false;
    String selectedPack = "";


    public PacksComponent() {


    }

    public void setupTextures(){
        try {
            panelBackGroundTEX = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/packPanel.png"));
            System.out.println(panelBackGroundTEX.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            play = new Icon("img/right.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        int i = 0;
        for (int j = 0; j < 45; j++) {
            if(scroll == scrolltwo){
                isScrolling = false;
            }
            if(isScrolling){
                if(scrolltwo > scroll){
                    scroll ++;
                } else {
                    scroll --;
                }
            }
        }

        for (ModPack pack : packs) {
            glEnable(GL_BLEND);
            Color.white.bind();
            Texture packTexture = pack.getTexture();
            if(packTexture != null){
                glBindTexture(GL_TEXTURE_2D, pack.getTexture().getTextureID());
            } else {
                glBindTexture(GL_TEXTURE_2D, panelBackGroundTEX.getTextureID());
            }

            GL11.glBegin(GL11.GL_QUADS);
            int x = (i * 550) + 10 + scroll;
            int y = 200;
          //  if(souldRenderAt(x)){
                //TODO fade out
                GL11.glTexCoord2f(0, 0);
                GL11.glVertex2f(x, y);

                GL11.glTexCoord2f(1, 0);
                GL11.glVertex2f(x + panelBackGroundTEX.getTextureWidth(), y);

                GL11.glTexCoord2f(1, 1);
                GL11.glVertex2f(x + panelBackGroundTEX.getTextureWidth(), y + panelBackGroundTEX.getTextureHeight());

                GL11.glTexCoord2f(0, 1);
                GL11.glVertex2f(x, y + panelBackGroundTEX.getTextureHeight());

                GL11.glEnd();
                i++;
                if (OpenLauncherGui.HEIGHT - Mouse.getY() > y && OpenLauncherGui.HEIGHT - Mouse.getY() < y + panelBackGroundTEX.getTextureHeight()) {
                    if (Mouse.getX() > x && Mouse.getX() < x + panelBackGroundTEX.getTextureWidth()) {
                        Color.white.bind();
                        play.glBindTexture();
                        if(selectedPack.equals(pack.getInstanceName())){
                            play.render(x + 100, y, 0, 0, play.getWidth(), play.getHeight(), 0);
                        }

                        if (Mouse.isButtonDown(0)) {
                            System.out.println(pack.getInstanceName());
                            int d = 0;
                            for(ModPack packname : packs){
                                if(packname.getInstanceName().equals(pack.getInstanceName())){
                                    if(selectedPack.equals(pack.getInstanceName()) && !isScrolling){
                                        ModPack launchPack = null;
                                        for (ModPack modPack : Launch.modPacks) {
                                            if (modPack.getInstanceName().equals(selectedPack)) {
                                                launchPack = modPack;
                                            }
                                        }
                                        try {
                                            new ModPackInstaller().playPack(launchPack);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    scrolltwo = -(d * 550 -550);
                                    isScrolling = true;
                                    selectedPack = pack.getInstanceName();
                                }
                                d++;
                            }
                        }
                    }
                }
          //  }
        }
    }


    public boolean souldRenderAt(int x) {
        System.out.println(x);
        if ( x > OpenLauncherGui.WIDTH){
            return false;
        }
        return x < -600;
    }

    @Override
    public void input() {
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            scrolltwo += 550;
            isScrolling = true;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            scrolltwo -= 550;
            isScrolling = true;
        }
    }

    @Override
    public void logic(int delta) {

    }
}
