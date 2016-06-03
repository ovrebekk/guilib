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
    public static float         mCentiMeterToPixelRatio = 42.0f;

    public static int screenHeight = 0; // Used to invert Y coordinates

    public enum PivotPoint {
        TOP_LEFT, MID_LEFT, BOTTOM_LEFT, TOP_MID, MID_MID, BOTTOM_MID, TOP_RIGHT, MID_RIGHT, BOTTOM_RIGHT
    }

    GuiElement  mBaseGuiElement;

    public GuiManager(){
        mBaseGuiElement = new GuiElement(new GuiRectangle(0, 0, 100, 100));
    }

    public void addTopLevelElement(GuiElement element){
        mBaseGuiElement.addChild(element);
    }

    public void draw(SpriteBatch spriteBatch) {
        mBaseGuiElement.draw(spriteBatch);
    }

    public void resize(int width, int height){
        screenHeight = height;
        mBaseGuiElement.setRegion(new GuiRectangle(0, 0, width, height));
        mBaseGuiElement.invalidate(null);
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return mBaseGuiElement.touchDown(x, screenHeight-y, pointer, button);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return mBaseGuiElement.touchUp(x, screenHeight-y, pointer, button);
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return mBaseGuiElement.touchDragged(x, screenHeight-y, pointer);
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
