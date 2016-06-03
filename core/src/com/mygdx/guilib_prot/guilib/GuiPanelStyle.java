package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by ovreb on 02.06.2016.
 */
public class GuiPanelStyle {
    private GuiAssetManager mAssetManager;
    private String          mBckImage;

    public GuiPanelStyle(GuiAssetManager assMan, String imgBackground){
        mAssetManager = assMan;
        mBckImage = imgBackground;
    }

    public Texture getTextureBackground(){
        return mAssetManager.getTexture(mBckImage);
    }
}
