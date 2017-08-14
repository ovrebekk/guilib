package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;

import java.util.List;
import java.util.Map;

public class GuiLabel extends GuiElement {

    private String      mText;
    private Color       mTextColor;
    private BitmapFont  mFont;
    private GlyphLayout mGlyphLayout;

    public GuiLabel(XmlReader.Element xmlElement, XmlReader.Element xmlRootElement, int tabSpaces) {
        // Start by getting the core parameters
        getBasicParametersFromXml(xmlElement, xmlRootElement);

        // Get label parameters
        mText = getXmlAttributeIfPresent(xmlElement, "text");
        GuiXmlParser.checkForReferenceAndRegister(mText);
        mTextColor = GuiXmlParser.stringToColor(getXmlAttributeIfPresent(xmlElement, "textColor"));
        mFont = new BitmapFont();
        mFont.setColor(mTextColor != null ? mTextColor : Color.WHITE);

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

    public static void checkXmlElementForReferences(XmlReader.Element xmlElement, List<String> referenceList){
        // Check if the text parameter contains a reference
        String textReference = GuiXmlParser.isReference(getXmlAttributeIfPresent(xmlElement, "text"));
        if(textReference != null && !referenceList.contains(textReference)){
            referenceList.add(textReference);
        }
    }

    @Override
    public void publishReferenceParameters(Map<String, String> referenceList) {
        // The text parameter of a label is a candidate for a reference
        String reference = GuiXmlParser.isReference(mText);
        if(reference != null){
            if(!referenceList.containsKey(reference)){
                // Putting the key without any value means we don't have the data available, and need to have it provided
                referenceList.put(reference, null);
            }
        }

        // Call the super function, which forwards the call to the children
        super.publishReferenceParameters(referenceList);
    }

    @Override
    public void pushReferences(Map<String, String> referenceMap) {
        // Check the text parameter for references
        String textReference = GuiXmlParser.isReference(mText);
        if(textReference != null && referenceMap.containsKey(textReference)){
            mText = referenceMap.get(textReference);
        }

        // Run the base function (includes children)
        super.pushReferences(referenceMap);
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
