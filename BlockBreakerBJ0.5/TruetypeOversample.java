/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
//package org.lwjgl.demo.stb;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.Callback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * STB Truetype oversampling demo.
 *
 * <p>This is a Java port of <a href="https://github.com/nothings/stb/blob/master/tests/oversample/main.c">https://github
 * .com/nothings/stb/blob/master/tests/oversample/main.c</a>.</p>
 */
public class TruetypeOversample {

    private static final int BITMAP_W = 512;
    private static final int BITMAP_H = 512;

    private static final float[] scale = {
            28.0f,
            14.0f
        };

    // ----

    private final STBTTAlignedQuad q  = STBTTAlignedQuad.malloc();
    private final FloatBuffer      xb = memAllocFloat(1);
    private final FloatBuffer      yb = memAllocFloat(1);

    private long window;

    private Callback debugProc;

    // ----

    private int ww = 1024;
    private int wh = 768;

    private int font_tex;

    private STBTTPackedchar.Buffer chardata;


    private boolean black_on_white;
    private boolean integer_align;


    /**
     * TruetypeOversample Constructor
     *
     * @param window Window for the fonts to be applied
     */
    public TruetypeOversample(long window, String font)
    {
        load_fonts(font);
        this.window = window;		
    }

    /**
     * Method load_fonts
     *
     */
    private void load_fonts(String font) 
    {
        font_tex = glGenTextures();
        chardata = STBTTPackedchar.malloc(6 * 128);

        try ( STBTTPackContext pc = STBTTPackContext.malloc() ) {
            ByteBuffer ttf = IOUtil.ioResourceToByteBuffer(font, 160 * 1024);

            ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);

            stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
            for ( int i = 0; i < 2; i++ ) {
                int p = (i * 3 + 0) * 128 + 32;
                chardata.limit(p + 95);
                chardata.position(p);
                stbtt_PackSetOversampling(pc, 1, 1);
                stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, chardata);

                p = (i * 3 + 1) * 128 + 32;
                chardata.limit(p + 95);
                chardata.position(p);
                stbtt_PackSetOversampling(pc, 2, 2);
                stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, chardata);

                p = (i * 3 + 2) * 128 + 32;
                chardata.limit(p + 95);
                chardata.position(p);
                stbtt_PackSetOversampling(pc, 3, 1);
                stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, chardata);
            }
            chardata.clear();
            stbtt_PackEnd(pc);

            glBindTexture(GL_TEXTURE_2D, font_tex);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   

    /**
     * Method drawBoxTC
     *
     * @param x0 A parameter
     * @param y0 A parameter
     * @param x1 A parameter
     * @param y1 A parameter
     * @param s0 A parameter
     * @param t0 A parameter
     * @param s1 A parameter
     * @param t1 A parameter
     */
    private void drawBoxTC(float x0, float y0, float x1, float y1, float s0, float t0, float s1, float t1) 
    {
        glTexCoord2f(s0, t0);
        glVertex2f(x0, y0);
        glTexCoord2f(s1, t0);
        glVertex2f(x1, y0);
        glTexCoord2f(s1, t1);
        glVertex2f(x1, y1);
        glTexCoord2f(s0, t1);
        glVertex2f(x0, y1);
    }

    /**
     * Method print
     *
     * @param x A parameter
     * @param y A parameter
     * @param font A parameter
     * @param text A parameter
     */
    private void print(float x, float y, int font, String text) 
    {
        xb.put(0, x);
        yb.put(0, y);

        chardata.position(font * 128);

        glEnable(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, font_tex);		

        glBegin(GL_QUADS);
        for ( int i = 0; i < text.length(); i++ ) {
            stbtt_GetPackedQuad(chardata, BITMAP_W, BITMAP_H, text.charAt(i), xb, yb, q, font == 0 && integer_align);
            drawBoxTC(
                q.x0(), q.y0(), q.x1(), q.y1(),
                q.s0(), q.t0(), q.s1(), q.t1()
            );
        }
        glEnd();
    }

    /**
     * Method draw_world
     *
     * @param inputString A parameter
     */
    public void draw_world(String inputString, int posX, int getY, float red, float green, float blue) 
    {
    	glLoadIdentity();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if ( black_on_white )
        {
            glColor4f(red, green, blue, 0.2f);
        }
        else
        {    
        	glColor4f(red, green, blue, 5.0f);  // Font color
        }

        glOrtho(0.0, ww, wh, 0.0, -0.0, 0.9);
        print(posX, getY, 0, inputString);        

        
        glDisable(GL_TEXTURE_2D);

    }   

    /**
     * Method windowSizeChanged
     *
     * @param window A parameter
     * @param width A parameter
     * @param height A parameter
     */
    public void windowSizeChanged(long window, int width, int height) 
    {
        this.ww = width;
        this.wh = height;
    }

   
    /**
     * Method destroy
     *
     */
    public void destroy() 
    {
        chardata.free();

        if ( debugProc != null )
        {
            debugProc.free();
        }

        glfwFreeCallbacks(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        memFree(yb);
        memFree(xb);

        q.free();
    }
}