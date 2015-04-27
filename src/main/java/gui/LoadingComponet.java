package gui;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;

public class LoadingComponet implements RenderComponet {

    Texture panelBackGroundTEX;
    TrueTypeFont font;

    public boolean isLoading = false;

    @Override
    public void render() {

        //TODO make this pretty later
        if(isLoading)
            font.drawString(OpenLauncherGui.WIDTH / 2, OpenLauncherGui.HEIGHT / 2, "Loading!!!! Please wait a while.", org.newdawn.slick.Color.white);
    }

    @Override
    public void input() {

    }

    @Override
    public void logic(int delta) {

    }

    public void setupTextures(){
        try {
            panelBackGroundTEX = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/packPanel.png"));
            System.out.println(panelBackGroundTEX.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font awtFont = new Font("Latha", Font.BOLD, 24);
        font = new TrueTypeFont(awtFont, false);
    }
}
