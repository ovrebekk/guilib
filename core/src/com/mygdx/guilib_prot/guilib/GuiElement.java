package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ovreb on 31.05.2016.
 */
public class GuiElement {
    public enum AlignmentToParent {

    }

    protected GuiRectangle        mRegion;
    protected GuiElement          mParent;
    protected List<GuiElement>    mChildren;

    // Elementary properties
    protected boolean             mEnabled;
    // TODO: Consider padding?

    public GuiElement(GuiRectangle region){
        mChildren = null;
        mRegion = region;
        mParent = null;
        mEnabled = true;
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
}
