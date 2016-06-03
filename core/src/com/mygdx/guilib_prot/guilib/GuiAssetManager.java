package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

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
        }
        return new Texture(handle);
    }
}
