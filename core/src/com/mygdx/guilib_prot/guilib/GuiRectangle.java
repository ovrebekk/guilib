package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;

/**
 * Created by ovreb on 31.05.2016.
 */
public class GuiRectangle {
    private GuiDimension mPosX;
    private GuiDimension mPosY;
    private GuiDimension mWidth;
    private GuiDimension mHeight;
    private GuiManager.PivotPoint mPivot;

    private float mCachedX, mCachedY, mCachedW, mCachedH;

    public GuiRectangle(float posX, float posY, float width, float height){
        mPosX = new GuiDimension(posX);
        mPosY = new GuiDimension(posY);
        mWidth = new GuiDimension(width);
        mHeight = new GuiDimension(height);
        mPivot = GuiManager.PivotPoint.BOTTOM_LEFT;
    }

    public GuiRectangle(float posX, float posY, float width, float height, GuiManager.PivotPoint pivot){
        mPosX = new GuiDimension(posX);
        mPosY = new GuiDimension(posY);
        mWidth = new GuiDimension(width);
        mHeight = new GuiDimension(height);
        mPivot = pivot;
    }

    public GuiRectangle(GuiDimension posX, GuiDimension posY, GuiDimension width, GuiDimension height){
        mPosX = posX;
        mPosY = posY;
        mWidth = width;
        mHeight = height;
        mPivot = GuiManager.PivotPoint.BOTTOM_LEFT;
    }

    public GuiRectangle(String posX, String posY, String width, String height){
        mPosX = new GuiDimension(posX);
        mPosY = new GuiDimension(posY);
        mWidth = new GuiDimension(width);
        mHeight = new GuiDimension(height);
        mPivot = GuiManager.PivotPoint.BOTTOM_LEFT;
    }

    public GuiRectangle(String posX, String posY, String width, String height, GuiManager.PivotPoint pivot){
        mPosX = new GuiDimension(posX);
        mPosY = new GuiDimension(posY);
        mWidth = new GuiDimension(width);
        mHeight = new GuiDimension(height);
        mPivot = pivot;
    }

    // Copy constructor
    public GuiRectangle(GuiRectangle guiRectangle){
        mPosX = new GuiDimension(guiRectangle.mPosX);
        mPosY = new GuiDimension(guiRectangle.mPosY);
        mWidth = new GuiDimension(guiRectangle.mWidth);
        mHeight = new GuiDimension(guiRectangle.mHeight);

        // This might just set the reference. Probably OK if you don't change the pivot dynamically
        mPivot = guiRectangle.mPivot;
    }

    public GuiRectangle offsetClone(int x, int y){
        GuiRectangle offsetRectangle = new GuiRectangle(this);
        offsetRectangle.mPosX.add(new GuiDimension((float)x));
        offsetRectangle.mPosY.add(new GuiDimension((float)y));
        return offsetRectangle;
    }

    public void setX(String x){ mPosX = new GuiDimension(x);}

    public void setY(String y) { mPosY = new GuiDimension(y);}

    public void setW(String w) { mWidth = new GuiDimension(w);}
    public void setW(float w){ mWidth = new GuiDimension(w); }

    public void setH(String h) { mHeight = new GuiDimension(h);}
    public void setH(float h){ mHeight = new GuiDimension(h); }


    public float getX(){
        return mCachedX;
    }

    public float getY(){
        return mCachedY;
    }

    public float getInvY() { return GuiManager.screenHeight - mCachedY; }

    public float getW(){
        return mCachedW;
    }

    public float getH() { return mCachedH; }

    public void incrementX(float amount) { mPosX.incrementPixel(amount); }

    public void incrementY(float amount) { mPosY.incrementPixel(amount); }

    public boolean contains(int x, int y){
        return (float)x >= mCachedX && (float)x < (mCachedX + mCachedW) && (float)y >= mCachedY && (float)y < (mCachedY + mCachedH);
    }

    public void invalidate(GuiElement parent){
        mPosX.invalidate(parent);
        mPosY.invalidate(parent);
        mWidth.invalidate(parent);
        mHeight.invalidate(parent);
        if(parent != null) {
            switch (mPivot) {
                case BOTTOM_LEFT:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension();
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension();
                    break;

                case MID_LEFT:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension();
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension() - mHeight.getPixelDimension() * 0.5f;
                    break;

                case TOP_LEFT:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension();
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension() - mHeight.getPixelDimension();
                    break;

                case BOTTOM_MID:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension() - mWidth.getPixelDimension() * 0.5f;
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension();
                    break;

                case MID_MID:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension() - mWidth.getPixelDimension() * 0.5f;
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension() - mHeight.getPixelDimension() * 0.5f;
                    break;

                case TOP_MID:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension() - mWidth.getPixelDimension() * 0.5f;
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension() - mHeight.getPixelDimension();
                    break;

                case BOTTOM_RIGHT:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension() - mWidth.getPixelDimension();
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension();
                    break;

                case MID_RIGHT:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension() - mWidth.getPixelDimension();
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension() - mHeight.getPixelDimension() * 0.5f;
                    break;

                case TOP_RIGHT:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension() - mWidth.getPixelDimension();
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension() - mHeight.getPixelDimension();
                    break;

                case CENTER:
                    mCachedX = parent.getRegion().getX() + mPosX.getPixelDimension() - (mWidth.getPixelDimension() / 2);
                    mCachedY = parent.getRegion().getY() + mPosY.getPixelDimension() - (mHeight.getPixelDimension() / 2);
                    break;

                default:
                    Gdx.app.log("GuiRectangle", "PivotPoint type not implemented!!!");
                    break;
            }
        }
        else {
            mCachedX = mPosX.getPixelDimension();
            mCachedY = mPosY.getPixelDimension();
        }
        mCachedW = mWidth.getPixelDimension();
        mCachedH = mHeight.getPixelDimension();
    }
}
