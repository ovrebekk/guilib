package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;

public class GuiLabel extends GuiElement {

    private String      mText;
    private Color       mTextColor;
    private BitmapFont  mFont;
    private GlyphLayout mGlyphLayout;

    public GuiLabel(XmlReader.Element xmlElement, XmlReader.Element xmlRootElement, int tabSpaces) {
        // Start by getting the core parameters
        getBasicParametersFromXml(xmlElement, xmlRootElement);

        // Get label parameters
        mText = getXmlAttributeIfPresent(xmlElement, "text", "");
        mTextColor = GuiXmlParser.stringToColor(getXmlAttributeIfPresent(xmlElement, "textColor", "255,255,255"));
        mFont = new BitmapFont();
        mFont.setColor(mTextColor);

        // Write a debug message
        String tag = "GuiLabel";
        for(int i = 0; i < tabSpaces; i++) tag = "  " + tag;
        Gdx.app.log(tag, "New element from XML: " + mId);
    }

    // Copy constructor
    public GuiLabel(GuiLabel cloneElement, GuiElement parent) {
        super(cloneElement, parent);
        mText = cloneElement.mText;
        mTextColor = cloneElement.mTextColor;
        mFont = cloneElement.mFont;
    }

    @Override
    public void invalidate(GuiElement parent) {
        mGlyphLayout = new GlyphLayout(mFont, mText);
        mRegion.setW(mGlyphLayout.width);
        mRegion.setH(mGlyphLayout.height);
        mRegion.invalidate(parent);
        super.invalidate(parent);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if(mFont != null && mText != null){
            mFont.draw(spriteBatch, mText, mRegion.getX(), mRegion.getY());
        }
    }
}
