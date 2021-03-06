package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected Map<String, String> mReferenceMap;

    // Elementary properties
    protected boolean             mEnabled;
    protected boolean             mDebugMode;       // Used to draw debug info on gui elements
    // TODO: Consider padding?

    public GuiElement() {
        mChildren = null;
        mRegion = null;
        mParent = null;
        mEnabled = true;
        mReferenceMap = null;
    }

    // Copy constructor
    public GuiElement(GuiElement clone, GuiElement parent){
        mId = clone.mId;
        mRegion = new GuiRectangle(clone.mRegion);
        mStateFlags = clone.mStateFlags;
        mEnabled = clone.mEnabled;
        mReferenceMap = null;

        // We won't touch the children members, since we need to point to the cloned parent and children
    }

    public GuiElement(GuiRectangle region){
        mChildren = null;
        mRegion = region;
        mParent = null;
        mEnabled = true;
        mReferenceMap = null;
    }

    public String getId(){ return mId; }

    public Map<String, String> getReferenceMap(){ return mReferenceMap; }

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
    protected static String getXmlAttributeIfPresent(XmlReader.Element xmlElement, String attributeName) {
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

        // Check the list of global references, and see if any of them match an attribute in the XML
        resolveReferences(xmlElement);
    }

    // Store the value of any attribute that matches the global reference list
    public void resolveReferences(XmlReader.Element xmlElement){
        for(String reference : GuiXmlParser.XmlAttributeReferenceList){
            String referenceValue = getXmlAttributeIfPresent(xmlElement, reference);
            if(referenceValue != null){
                if(mReferenceMap == null) mReferenceMap = new HashMap<String, String>();
                mReferenceMap.put(reference, referenceValue);
            }
        }
    }

    // A parent can use this function to push references to its children
    public void pushReferences(Map<String, String> referenceMap){
        // Check local parameters that might be referenced

        // Forward the call recursively to children
        if(mChildren != null) {
            for(GuiElement child : mChildren){
                child.pushReferences(referenceMap);
            }
        }
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

    public boolean pushDebugCommand(String command){
        boolean returnValue = false;
        // Process incomming commands
        if(command.startsWith("debug ")){
            if(command.endsWith(" on")){
                mDebugMode = true;
            }
            else if(command.endsWith(" off")){
                mDebugMode = false;
            }
            else if(command.endsWith(" toggle")){
                mDebugMode = !mDebugMode;
                //Gdx.app.log("GuiElement", "Debug mode toggle");
            }
        }

        // Forward command to children
        if(mChildren != null){
            for(GuiElement child : mChildren){
                if(returnValue) return true;
                returnValue = child.pushDebugCommand(command);
            }
        }
        // A return value of true signals that the command is consumed, and should not be propagated further
        return returnValue;
    }


    // References are used to allow a template element to have open fields that can be replaced by the inheriter
    // This function allows the GuiElement to report which references it is looking for
    public void publishReferenceParameters(Map<String, String> referenceList){
        // If any local parameters need checking, do it here

        // Forward the call to all the children
        if(mChildren != null){
            for(GuiElement child : mChildren) {
                // For each child, forward the map
                child.publishReferenceParameters(referenceList);
                // Then we check if we can provide the reference parameters
            }
        }
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
        if(mDebugMode){
            spriteBatch.end();
            GuiManager.mShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            GuiManager.mShapeRenderer.setColor(Color.RED);
            GuiManager.mShapeRenderer.rect(mRegion.getX(), mRegion.getY(), mRegion.getW(), mRegion.getH());
            GuiManager.mShapeRenderer.end();
            spriteBatch.begin();
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
