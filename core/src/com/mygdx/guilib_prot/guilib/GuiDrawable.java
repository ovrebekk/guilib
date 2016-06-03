package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ovreb on 03.06.2016.
 */
public class GuiDrawable {
    public Texture mTexture;
    public NinePatch mNinePatch;

    public GuiDrawable(){
        mTexture = null;
        mNinePatch = null;
    }

    public GuiDrawable(Texture texture){
        mTexture = texture;
        mNinePatch = null;
    }

    public GuiDrawable(NinePatch ninePatch){
        mTexture = null;
        mNinePatch = ninePatch;
    }

    public void draw(SpriteBatch spriteBatch, GuiRectangle drawRegion){
        if(mTexture != null){
            spriteBatch.draw(mTexture, drawRegion.getX(), drawRegion.getY(), drawRegion.getW(), drawRegion.getH());
        }
        if(mNinePatch != null){
            mNinePatch.draw(spriteBatch, drawRegion.getX(), drawRegion.getY(), drawRegion.getW(), drawRegion.getH());
        }
    }
}
