package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ovreb on 02.06.2016.
 */
public class GuiPanel extends GuiElement {
    private GuiDrawable     mImgBackground;
    private GuiPanelStyle   mStyle;

    // Extended properties
    private boolean         mMovable;

    // Internal state variables
    private boolean         mInDragMode = false;
    private int             mLastMouseX, mLastMouseY;

    public GuiPanel(GuiPanelStyle style, GuiRectangle region) {
        super(region);
        mStyle = style;
        mImgBackground = new GuiDrawable(new NinePatch(style.getTextureBackground(), 40, 40, 40, 40));
        mImgBackground.setRegion(region);
        mMovable = false;
    }

    public void setMovable(boolean movable) { mMovable = movable; }

    @Override
    public void draw(SpriteBatch spriteBatch){
        if(mEnabled) {
            mImgBackground.draw(spriteBatch);//, mRegion);
        }
        super.draw(spriteBatch);
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

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        boolean returnValue = false;
        if(mRegion.contains(x, y)){
            if(mChildren != null){
                for (int i = 0; i < mChildren.size(); i++){
                    if(mChildren.get(i).touchDown(x, y, pointer, button)){
                        returnValue = true;
                    }
                }
            }
            if(mMovable) {
                // Nothing else clicked. Enter drag mode.
                if (!returnValue) {
                    mInDragMode = true;
                    mLastMouseX = x;
                    mLastMouseY = y;
                }
                return true;
            }
        }
        return returnValue;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        boolean returnValue = false;
        // NOTE: We don't check region.contains(..) on touchUp, since this can typically happen after the cursor has left the element
        if(mChildren != null){
            for (int i = 0; i < mChildren.size(); i++){
                if(mChildren.get(i).touchUp(x, y, pointer, button)){
                    returnValue = true;
                }
            }
        }
        mInDragMode = false;
        return returnValue;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        if(mInDragMode){
            mRegion.incrementX(x - mLastMouseX);
            mRegion.incrementY(y - mLastMouseY);
            invalidate(mParent);
            mLastMouseX = x;
            mLastMouseY = y;
            return true;
        }
        return false;
    }
}
