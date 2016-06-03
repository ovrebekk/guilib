package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ovreb on 31.05.2016.
 */
public class GuiButton extends GuiElement {
    GuiDrawable     mImgBackground;
    GuiDrawable     mImgPressed;
    String          mText;
    GuiButtonStyle  mStyle;

    // Button state variables
    boolean mPressed = false;

    public GuiButton(GuiButtonStyle style, String text, GuiRectangle region){
        super(region);
        NinePatch npBack = new NinePatch(style.getTextureBackground(), 14, 16, 14, 16);
        mImgBackground = new GuiDrawable(npBack);
        NinePatch npPressed = new NinePatch(style.getTexturePressed(), 14, 16, 14, 16);
        mImgPressed = new GuiDrawable(npPressed);
        mText = text;
        mStyle = style;
    }

    @Override
    public void draw(SpriteBatch spriteBatch){
        if(mPressed) {
            mImgPressed.draw(spriteBatch, mRegion);
            mStyle.getTextFont().draw(spriteBatch, mText, mRegion.getX()+15.0f, mRegion.getY() + mRegion.getH()*0.5f);
        }
        else {
            mImgBackground.draw(spriteBatch, mRegion);
            mStyle.getTextFont().draw(spriteBatch, mText, mRegion.getX()+15.0f, mRegion.getY() + mRegion.getH()*0.5f);
        }
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        if(mRegion.contains(x, y)){
            mPressed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        if(mPressed) {
            mPressed = false;
            return true;
        }
        return false;
    }
}
