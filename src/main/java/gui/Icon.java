package gui;


import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Icon {
    int toggle;
    boolean tex;
    private int height;
    private int width;
    private int texID;
    private ByteBuffer data;
    private IntBuffer dataInt;

    /**
     * Load the specified texture and bind it into OpenGL
     *
     * @param filename Filename to load
     * @throws IOException If there are errors loading image
     */
    public Icon(String filename) throws IOException {
        // BufferedImage img = ImageIO.read(new File(filename));
        BufferedImage img = ImageIO.read(ClassLoader.getSystemResourceAsStream(filename));
        width = img.getWidth();
        height = img.getHeight();
        data = BufferUtils.createByteBuffer(height * width * 4);

        // Some wierd flip-flop that gets it oriented right (I think.... >.>)
        for (int x = height - 1; x > -1; x--) {
            for (int y = 0; y < width; y++) {
                int rgba = img.getRGB(y, x);
                byte b = (byte) (rgba >> 0 & 0xFF);
                byte g = (byte) (rgba >> 8 & 0xFF);
                byte r = (byte) (rgba >> 16 & 0xFF);
                byte a = (byte) (rgba >> 24 & 0xFF);
                data.put(r);
                data.put(g);
                data.put(b);
                data.put(a);
            }
        }
        data.rewind();
        texID = bindToTexture();
        data = null;
    }

    public Icon(int[] pixels, int width, int height) {
        // BufferedImage img = ImageIO.read(new File(filename));
        this.width = width;
        this.height = height;
        dataInt = ByteBuffer.allocateDirect(pixels.length * 4).asIntBuffer();
        dataInt.put(pixels);
        dataInt.flip();
        texID = bindToTexture();
        dataInt = null;
    }

    /**
     * Calls glBindTexture(GL_TEXTURE_2D, textureID)
     */
    public void glBindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
    }

    /**
     * Get the height of this texture in pixels
     *
     * @return Height of this texture in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the width of this texture in pixels
     *
     * @return Widht of this texture in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the OpenGL texture ID used by this texture
     *
     * @return OpenGL texture ID
     */
    public int getTexID() {
        return texID;
    }

    /**
     * Returns an int color of the 4 provided colors (they should be in the
     * range 0-255)
     *
     * @param r Red component of color
     * @param g Green component of color
     * @param b Blue component of color
     * @param a Alpha component of color
     * @return Integer version of color provided
     */
    public int intColor(short r, short g, short b, short a) {
        int out = 0;
        out |= (a & 0x00FF) << 0;
        out |= (b & 0x00FF) << 8;
        out |= (g & 0x00FF) << 16;
        out |= (r & 0x00FF) << 24;
        return out;
    }

    /**
     * Render this texture at a 1:1 ratio
     *
     * @param startX X position to render at
     * @param startY Y position to render at
     * @param texX   X position to start at of texture in pixels
     * @param texY   Y position to start at of texture in pixels
     * @param width  Width to render
     * @param height Height to render
     */
    public void render(float startX, float startY, float texX, float texY, float width, float height) {
        this.render(startX, startY, texX, texY, width, height, 0);
    }

    /**
     * Render this texture at a 1:1 ratio with rotation
     *
     * @param startX X position to render at
     * @param startY Y position to render at
     * @param texX   X position to start at of texture in pixels
     * @param texY   Y position to start at of texture in pixels
     * @param width  Width to render
     * @param height Height to render
     * @param rot    Angle to render at
     */
    public void render(float startX, float startY, float texX, float texY, float width, float height, float rot) {
        float one = 1.0F / (float) getWidth();
        // texY = 1024 - texY;
        texY = getHeight() - height - texY;
        float w = width;
        float h = height;

        GL11.glBindTexture(GL_TEXTURE_2D, texID);
        GL11.glPushMatrix();
        GL11.glTranslatef(startX, startY, 0);
        GL11.glRotatef(rot, 0, 0, 1);
        glBegin(GL_QUADS);
        glColor4f(1, 1, 1, 1);

        glTexCoord2f(texX * one, texY * one);
        glVertex2f(0, 0);

        glTexCoord2f(texX * one + w * one, texY * one);
        glVertex2f(w, 0);

        glTexCoord2f(texX * one + w * one, texY * one + h * one);
        glVertex2f(w, h);

        glTexCoord2f(texX * one, texY * one + h * one);
        glVertex2f(0, h);

        glEnd();
        GL11.glPopMatrix();
    }

    /**
     * Render this texture at a 1:1 ratio, centered at the position given
     *
     * @param startX X position to render at
     * @param startY Y position to render at
     * @param texX   X position to start at of texture in pixels
     * @param texY   Y position to start at of texture in pixels
     * @param width  Width to render
     * @param height Height to render
     * @param rot    Angle to render at
     */
    public void renderCentered(float startX, float startY, float texX, float texY, float width, float height, float rot) {
        float one = 1.0F / (float) getWidth();
        // texY = 1024 - texY;
        texY = getHeight() - height - texY;
        float w = width;
        float h = height;

        GL11.glBindTexture(GL_TEXTURE_2D, texID);
        GL11.glPushMatrix();
        GL11.glTranslatef(startX, startY, 0);
        GL11.glRotatef(rot, 0, 0, 1);
        glBegin(GL_QUADS);
        // glColor4f(1, 1, 1, 1);

        glTexCoord2f(texX * one, texY * one);
        glVertex2f(-w / 2, -h / 2);

        glTexCoord2f(texX * one + w * one, texY * one);
        glVertex2f(w / 2, -h / 2);

        glTexCoord2f(texX * one + w * one, texY * one + h * one);
        glVertex2f(w / 2, h / 2);

        glTexCoord2f(texX * one, texY * one + h * one);
        glVertex2f(-w / 2, h / 2);

        glEnd();
        GL11.glPopMatrix();
    }

    public void update(int[] pixels, int xOff, int yOff, int width, int height) {
        if (pixels.length != width * height) {
            throw new IllegalArgumentException("Array was of wrong length for this texture!");
        }
        if (xOff + width > this.width || yOff + height > this.height) {
            throw new IllegalArgumentException("Cannot update texture: would go out of bounds.");
        }
        dataInt = ByteBuffer.allocateDirect(pixels.length * 4).asIntBuffer();
        dataInt.put(pixels);
        dataInt.flip();
        updateData(texID, xOff, yOff, width, height);
        dataInt = null;
    }

    /**
     * Binds this texture to a new OpenGL texture
     *
     * @return The new OpenGL texture ID
     */
    private int bindToTexture() {
        int tex = GL11.glGenTextures();
        bindData(tex);
        return tex;
    }

    private void bindData(int tex) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST); // Linear Filtering
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // Linear Filtering
        if (data != null) {
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
        } else if (dataInt != null) {
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataInt);
        }
    }

    private void updateData(int tex, int xOff, int yOff, int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST); // Linear Filtering
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // Linear Filtering
        if (data != null) {
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, xOff, yOff, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
        } else if (dataInt != null) {
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, xOff, yOff, width, height, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataInt);
        }
    }
}
