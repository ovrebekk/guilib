package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;

/**oclassdeffounderror
 * Created by ovreb on 03.06.2016.
 */
public class GuiDrawable extends GuiElement {
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

    public GuiDrawable(XmlReader.Element xmlElement, XmlReader.Element xmlRootElement, int tabSpaces) {
        // Start by getting the core parameters
        getBasicParametersFromXml(xmlElement, xmlRootElement);

        // Get image parameters
        String imageName = getXmlAttributeIfPresent(xmlElement, "image");
        if(imageName != null) {
            mTexture = GuiElement.mGuiAssetManager.getTexture(imageName);
        }
        else {
            String ninepatchName = getXmlAttributeIfPresent(xmlElement, "ninepatch");
            String ninepatchCoordinates = getXmlAttributeIfPresent(xmlElement, "ninepatch_coordinates");
            if (ninepatchCoordinates != null && ninepatchCoordinates.split(",").length == 4) {
                int left = Integer.parseInt(ninepatchCoordinates.split(",")[0]);
                int right = Integer.parseInt(ninepatchCoordinates.split(",")[1]);
                int top = Integer.parseInt(ninepatchCoordinates.split(",")[2]);
                int bottom = Integer.parseInt(ninepatchCoordinates.split(",")[3]);
                mNinePatch = GuiElement.mGuiAssetManager.getNinePatch(ninepatchName, left, right, top, bottom);
            } else mNinePatch = null;
        }
        // Write a debug message
        String tag = "GuiDrawable";
        for(int i = 0; i < tabSpaces; i++) tag = "  " + tag;
        Gdx.app.log(tag, "New element from XML: " + mId);
    }

    // Copy constructor
    public GuiDrawable(GuiDrawable cloneElement, GuiElement parent) {
        super(cloneElement, parent);
        mTexture = cloneElement.mTexture;
        mNinePatch = cloneElement.mNinePatch;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if(mEnabled) {
            if (mTexture != null) {
                spriteBatch.draw(mTexture, mRegion.getX(), mRegion.getY(), mRegion.getW(), mRegion.getH());
            }
            if (mNinePatch != null) {
                mNinePatch.draw(spriteBatch, mRegion.getX(), mRegion.getY(), mRegion.getW(), mRegion.getH());
            }
        }
        super.draw(spriteBatch);
    }
}
