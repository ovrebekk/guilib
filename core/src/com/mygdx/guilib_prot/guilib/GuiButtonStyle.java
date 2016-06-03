package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by ovreb on 02.06.2016.
 */
public class GuiButtonStyle {
    private GuiAssetManager mAssetManager;
    private String          mBckImage;
    private String          mPressedImage;
    private BitmapFont      mTextFont;
    private Color           mTextColor;

    public GuiButtonStyle(GuiAssetManager assMan, String img, String imgPressed){
        mAssetManager = assMan;
        mBckImage = img;
        mPressedImage = imgPressed;
        mTextFont = new BitmapFont();
        mTextColor = Color.BLUE;
        mTextFont.setColor(mTextColor);
    }

    public Texture getTextureBackground(){
        return mAssetManager.getTexture(mBckImage);
    }

    public Texture getTexturePressed(){
        return mAssetManager.getTexture(mPressedImage);
    }

    public BitmapFont getTextFont(){
        return mTextFont;
    }
}
