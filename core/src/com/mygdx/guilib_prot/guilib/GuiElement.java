package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ovreb on 31.05.2016.
 */
public class GuiElement {
    public enum AlignmentToParent {

    }

    public enum GuiState {
        RELEASED, PRESSED, SELECTED, ALL;

        public int mask() {
            return 1 << ordinal();
        }
    }

    public static GuiAssetManager mGuiAssetManager;

    // Store the screen height. Used to invert the Y axis at the time of drawing.
    public static int mScreenHeight;

    protected String              mId;
    protected GuiRectangle        mRegion;
    protected GuiElement          mParent;
    protected List<GuiElement>    mChildren;

    protected int                 mStateFlags;

    protected String              mInheritFrom;

    // Elementary properties
    protected boolean             mEnabled;
    // TODO: Consider padding?

    public GuiElement() {
        mChildren = null;
        mRegion = null;
        mParent = null;
        mEnabled = true;
    }

    public GuiElement(GuiElement clone, GuiElement parent){
        mId = clone.mId;
        mRegion = new GuiRectangle(clone.mRegion);
        mStateFlags = clone.mStateFlags;
        mEnabled = clone.mEnabled;

        // We won't touch the children members, since we need to point to the cloned parent and children
    }

    public GuiElement(GuiRectangle region){
        mChildren = null;
        mRegion = region;
        mParent = null;
        mEnabled = true;
    }

    protected boolean isXmlAttributePresent(XmlReader.Element xmlElement, String attributeName){
        try {
            xmlElement.getAttribute(attributeName);
        }
        catch(GdxRuntimeException e) {
            return false;
        }
        return true;
    }

    // returns the value of the attribute if the attribute exists, and null otherwise
    protected String getXmlAttributeIfPresent(XmlReader.Element xmlElement, String attributeName) {
        String returnValue;
        try {
            returnValue = xmlElement.getAttribute(attributeName);
        }
        catch(GdxRuntimeException e) {
            returnValue = null;
        }
        return returnValue;
    }

    public GuiElement(XmlReader.Element xmlElement, XmlReader.Element xmlRootElement, int tabSpaces) {
        // Start by getting the core parameters
        getBasicParametersFromXml(xmlElement, xmlRootElement);

        // Write a debug message
        String tag = "GuiElement";
        for(int i = 0; i < tabSpaces; i++) tag = "  " + tag;
        Gdx.app.log(tag, "New element from XML: " + mId);
    }

    public String getId(){ return mId; }

    protected void getBasicParametersFromXml(XmlReader.Element xmlElement, XmlReader.Element xmlRootElement) {
        // Find the name. Set the name blank if not present
        if((mId = getXmlAttributeIfPresent(xmlElement, "name")) == null) mId = "NO_NAME";

        // Set the state flags
        mStateFlags = 0;
        String stateLabels = getXmlAttributeIfPresent(xmlElement, "state");
        if(stateLabels != null) {
            for (String state : stateLabels.split(",")) {
                for (int listIndex = 0; listIndex < (GuiState.values().length - 1); listIndex++) {
                    String test = GuiState.values()[listIndex].toString();
                    if (test.equals(state)) {
                        mStateFlags |= (1 << listIndex);
                        break;
                    }
                }
            }
        }
        else mStateFlags = 0xFFFFFFF;

        // Update the region
        mRegion = new GuiRectangle(getXmlAttributeIfPresent(xmlElement, "x"),
                                   getXmlAttributeIfPresent(xmlElement, "y"),
                                   getXmlAttributeIfPresent(xmlElement, "width"),
                                   getXmlAttributeIfPresent(xmlElement, "height"),
                                   getXmlAttributeIfPresent(xmlElement, "alignToParent"));

        // Inheritance parameters
        mInheritFrom = getXmlAttributeIfPresent(xmlElement, "inherit");
    }

    public void setParent(GuiElement parent){
        mParent = parent;
    }

    public void addChild(GuiElement child){
        if(mChildren == null){
            mChildren = new ArrayList<GuiElement>();
        }
        child.setParent(this);
        mChildren.add(child);
    }

    public List<GuiElement> getChildren(){ return mChildren; }

    public void setEnabled(boolean enabled){
        mEnabled = enabled;
    }

    public boolean getEnabled(){
        return mEnabled;
    }

    public void setRegion(GuiRectangle region){
        mRegion = region;
    }

    public GuiRectangle getRegion(){
        return mRegion;
    }

    public String getInheritFrom(){ return mInheritFrom; }

    public void inheritOpenParameters(GuiElement inheritTarget){
        mRegion.inheritFrom(inheritTarget.mRegion);

        // After resolving inheritance we set the inherit parameter to null
        // In this case we can run multiple inheritance passes, and only resolve each element once
        mInheritFrom = null;
    }

    public void draw(SpriteBatch spriteBatch){
        if(mEnabled){
            // Draw all children
            if(mChildren != null){
                for(int i = 0; i < mChildren.size(); i++){
                    mChildren.get(i).draw(spriteBatch);
                }
            }
        }
    }

    public void drawIfRightState(SpriteBatch spriteBatch, int stateMask){
        if((mStateFlags & stateMask) != 0){
            draw(spriteBatch);
        }
    }

    public void invalidate(GuiElement parent){
        // Invalidate local dimensjons
        mRegion.invalidate(parent);

        // Invalidate all children, setting yourself as parent (this)
        if(mChildren != null) {
            for (int i = 0; i < mChildren.size(); i++) {
                mChildren.get(i).invalidate(this);
            }
        }
    }

    // Input methods
    public boolean touchDown (int x, int y, int pointer, int button) {
        boolean returnValue = false;
        if(mRegion.contains(x, y)){
            if(mChildren != null){
                for (int i = 0; i < mChildren.size(); i++){
                    if(mChildren.get(i).touchDown(x, y, pointer, button)){
                        returnValue = true;
                        break;
                    }
                }
            }
        }
        return returnValue;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        boolean returnValue = false;
        // NOTE: We don't check region.contains(..) on touchUp, since this can typically happen after the cursor has left the element
        if(mChildren != null){
            for (int i = 0; i < mChildren.size(); i++){
                if(mChildren.get(i).touchUp(x, y, pointer, button)){
                    returnValue = true;
                    break;
                }
            }
        }
        return returnValue;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        boolean returnValue = false;
        // NOTE: We don't check region.contains(..) on touchDragged, since this can typically happen after the cursor has left the element
        if(mChildren != null){
            for (int i = 0; i < mChildren.size(); i++){
                if(mChildren.get(i).touchDragged(x, y, pointer)){
                    returnValue = true;
                    break;
                }
            }
        }
        return returnValue;
    }

    public boolean touchMove(int x, int y){
        if(mRegion.contains(x, y)){
            if(mChildren != null){
                for (int i = 0; i < mChildren.size(); i++){
                    if(mChildren.get(i).touchMove(x, y)){
                        break;
                    }
                }
            }
        }
        return false;
    }
}
