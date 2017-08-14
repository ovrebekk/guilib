package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ovreb on 31.05.2016.
 */
public class GuiManager implements InputProcessor {
    public static float mMilliMeterToPixelRatio = 42.0f / 10.0f;

    public static int screenWidth = 0, screenHeight = 0; // Used to invert Y coordinates

    public enum PivotPoint {
        TOP_LEFT, MID_LEFT, BOTTOM_LEFT, TOP_MID, MID_MID, BOTTOM_MID, TOP_RIGHT, MID_RIGHT, BOTTOM_RIGHT, CENTER
    }

    GuiElement  mBaseGuiElement;

    List<GuiElement> mLoadedGuiElements = new ArrayList<GuiElement>();
    GuiElement mActiveSceneElement = new GuiElement();

    public GuiManager(){

        mBaseGuiElement = new GuiElement(new GuiRectangle(0, 0, 100, 100));
    }

    private GuiElement guiElementInListById(String id, List<GuiElement> guiElementList){
        for(GuiElement element : guiElementList){
            if(element.getId().equals(id)){
                return element;
            }
        }
        return null;
    }

    private boolean recursiveGuiElementInheritanceSearch(GuiElement element, List<GuiElement> topElementList){
        boolean inheritanceExecuted = false;
        // Check the top guiElement for inheritance
        if(element.getInheritFrom() != null){
            GuiElement inheritanceTarget = guiElementInListById(element.getInheritFrom(), topElementList);
            if(inheritanceTarget != null){
                // Inherit the properties of the top element
                element.inheritOpenParameters(inheritanceTarget);

                // Inherit all the children of the top element
                for(GuiElement inheritTargetChild : inheritanceTarget.getChildren()){
                    element.addChild(cloneGuiElement(inheritTargetChild, element));
                }

                inheritanceExecuted = true;
            }
        }

        // Check the children for inheritance recursively
        if(element.getChildren() != null) {
            for (GuiElement elementChild : element.getChildren()) {
                inheritanceExecuted |=  recursiveGuiElementInheritanceSearch(elementChild, topElementList);
            }
        }

        // Return true if one or more elements went through the inheritance phase
        return inheritanceExecuted;
    }

    public void loadElementsFromFile(String fileName, boolean clearOldElements) {
        // Load all the GUI elements from the XML document
        List<GuiElement> elementList = GuiXmlParser.loadElementsFromFile(fileName);

        // Do inheritance pass to fill out inherited parameters
        for(GuiElement guiElement : elementList){
            recursiveGuiElementInheritanceSearch(guiElement, elementList);
        }

        if(clearOldElements) mLoadedGuiElements.clear();

        // Add all the elements to the element library
        mLoadedGuiElements.addAll(elementList);
    }

    public GuiElement spawnElement(String elementName, int x, int y) {
        GuiElement newElement = null;
        // First, try to find the element in the list of loaded gui elements
        for(GuiElement element : mLoadedGuiElements){
            if(element.getId().equals(elementName)){
                newElement = cloneGuiElement(element, null);
                newElement.setRegion(newElement.getRegion().offsetClone(x, y));
            }
        }
        if(newElement != null) {
            //newElement.invalidate(null);

            mActiveSceneElement.addChild(newElement);
            mActiveSceneElement.invalidate(null);
        }
        else {
            Gdx.app.error("GuiManager", "GuiElement not found: " + elementName);
        }
        return newElement;
    }

    public GuiElement cloneGuiElement(GuiElement guiElement, GuiElement parent){
        GuiElement newElement = null;
        if(guiElement.getClass() == GuiElement.class){
            newElement = new GuiElement(guiElement, parent);
        }
        else if(guiElement.getClass() == GuiButton.class){
            newElement = new GuiButton((GuiButton)guiElement, parent);
        }
        else if(guiElement.getClass() == GuiDrawable.class){
            newElement = new GuiDrawable((GuiDrawable)guiElement, parent);
        }
        else if(guiElement.getClass() == GuiLabel.class){
            newElement = new GuiLabel((GuiLabel)guiElement, parent);
        }

        // Clone the children
        if(newElement != null && guiElement.getChildren() != null){
            for(GuiElement child : guiElement.getChildren()){
                newElement.addChild(cloneGuiElement(child, newElement));
            }
        }

        return newElement;
    }

    public static PivotPoint pivotPointFromString(String xmlString){
        // Bottom left is the default value if the string is incorrect/missing
        PivotPoint newPivotPoint = GuiManager.PivotPoint.BOTTOM_LEFT;

        // If the string is not null, try to decode it
        if(xmlString != null) {
            try {
                newPivotPoint = GuiManager.PivotPoint.valueOf(xmlString);
            } catch (IllegalArgumentException e) {
                newPivotPoint = GuiManager.PivotPoint.BOTTOM_LEFT;
                Gdx.app.error("GuiElement", "Invalid alignToParent in XML: " + xmlString + ", defaulting to BOTTOM_LEFT");
            }
        }
        return newPivotPoint;
    }

    public void addTopLevelElement(GuiElement element){
        mBaseGuiElement.addChild(element);
    }

    public void draw(SpriteBatch spriteBatch) {
        //mBaseGuiElement.draw(spriteBatch);
        mActiveSceneElement.draw(spriteBatch);
    }

    public void resize(int width, int height){
        screenWidth = width;
        screenHeight = height;
        GuiElement.mScreenHeight = height;
        mBaseGuiElement.setRegion(new GuiRectangle(0, 0, width, height));
        mBaseGuiElement.invalidate(null);

        mActiveSceneElement.setRegion(new GuiRectangle(0, 0, width, height));
        mActiveSceneElement.invalidate(null);
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    static int spawnX = 0;
    static int spawnY = 0;

    @Override
    public boolean keyTyped(char c) {

        boolean keyHandled = true;
        if(c == 'e'){

            Gdx.app.log("GuiManager","Spawning GUI....");
            spawnElement("please_show", spawnX, spawnY);
            spawnX += 50;
            spawnY += 50;
        }
        // Reset layout and reload XML
        else if(c == 'r'){
            mActiveSceneElement = new GuiElement(mActiveSceneElement.getRegion());
            loadElementsFromFile(GuiAssetManager.defaultAssetDirectory + "\\layout_xml\\test_layouts.xml", true);
            spawnX = spawnY = 0;
        }
        else keyHandled = false;
        return keyHandled;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return mActiveSceneElement.touchDown(x, screenHeight-y, pointer, button);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return mActiveSceneElement.touchUp(x, screenHeight-y, pointer, button);
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return mActiveSceneElement.touchDragged(x, screenHeight-y, pointer);
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return mActiveSceneElement.touchMove(x, screenHeight - y);
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
