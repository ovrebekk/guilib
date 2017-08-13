package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 * Created by ovreb on 02.06.2016.
 */
public class GuiAssetManager {

    public static String defaultAssetDirectory;
    public static String imageNotFoundImage;

    public void GuiAssetManager(){

    }

    public Texture getTexture(String localFileName){
        FileHandle handle;
        handle = Gdx.files.internal(defaultAssetDirectory + localFileName + ".png");
        if(!handle.exists()){
            handle = Gdx.files.internal(defaultAssetDirectory + localFileName + ".jpg");
        }
        if(!handle.exists()){
            handle = Gdx.files.internal(imageNotFoundImage);
            Gdx.app.error("GuiAssetManager", "File not found: " + localFileName);
        }
        return new Texture(handle);
    }

    public NinePatch getNinePatch(String localFileName, int left, int right, int top, int bottom) {
        Texture ninePatchTexture = getTexture(localFileName);
        if(ninePatchTexture != null) {
            NinePatch newNinePatch = new NinePatch(ninePatchTexture, left, right, top, bottom);
            return newNinePatch;
        }
        else return null;
    }

    public BitmapFont getFont(){

        return null;
    }
}
