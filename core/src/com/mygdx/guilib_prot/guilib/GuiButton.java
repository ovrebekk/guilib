package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;

/**
 * Created by ovreb on 31.05.2016.
 */
public class GuiButton extends GuiElement {
    String          mText;

    // Button state variables
    boolean mPressed = false;
    boolean mSelected = false;

    @Deprecated
    public GuiButton(GuiButtonStyle style, String text, GuiRectangle region){
        super(region);
        mText = text;
        mRegion = region;
    }

    public GuiButton(XmlReader.Element xmlElement, XmlReader.Element xmlRootElement, int tabSpaces) {
        // Start by getting the core parameters
        getBasicParametersFromXml(xmlElement, xmlRootElement);

        // Write a debug message
        String tag = "GuiButton";
        for(int i = 0; i < tabSpaces; i++) tag = "  " + tag;
        Gdx.app.log(tag, "New element from XML: " + mId);
    }

    // Copy constructor
    public GuiButton(GuiButton cloneElement, GuiElement parent){
        super(cloneElement, parent);
        mText = cloneElement.mText;
    }

    @Override
    public void draw(SpriteBatch spriteBatch){
        if(mPressed) {
            // Draw all children
            if(mChildren != null){
                for(int i = 0; i < mChildren.size(); i++){
                    mChildren.get(i).drawIfRightState(spriteBatch, GuiState.PRESSED.mask());
                }
            }
        }
        else {
            if(mSelected){
                for(int i = 0; i < mChildren.size(); i++){
                    mChildren.get(i).drawIfRightState(spriteBatch, GuiState.SELECTED.mask());
                }
            }
            else {
                // Draw all children
                if (mChildren != null) {
                    for (int i = 0; i < mChildren.size(); i++) {
                        mChildren.get(i).drawIfRightState(spriteBatch, GuiState.RELEASED.mask());
                    }
                }
            }
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

    @Override
    public boolean touchMove(int x, int y) {
        if(mRegion.contains(x, y)){
            mSelected = !mPressed;
        }
        else mSelected = false;
        return false;
    }
}
