package com.mygdx.guilib_prot.guilib;

/**
 * Created by ovreb on 31.05.2016.
 */
public class GuiDimension {
    private float   mAmountPixel;
    private float   mAmountCentiMeter;
    private float   mParentWidthFactor;
    private float   mParentHeightFactor;

    private float   mCachedValue;

    public GuiDimension(float amPixel){
        mAmountPixel = amPixel;
        mAmountCentiMeter = 0.0f;
        mParentWidthFactor = 0.0f;
        mParentHeightFactor = 0.0f;
        invalidate(null);
    }

    public GuiDimension(float amPixel, float amDIPixel){
        mAmountPixel = amPixel;
        mAmountCentiMeter = amDIPixel;
        mParentWidthFactor = 0.0f;
        mParentHeightFactor = 0.0f;
        invalidate(null);
    }

    public GuiDimension(float amPixel, float amDIPixel, float parentWidthFactor, float parentHeightFactor){
        mAmountPixel = amPixel;
        mAmountCentiMeter = amDIPixel;
        mParentWidthFactor = parentWidthFactor;
        mParentHeightFactor = parentHeightFactor;
        invalidate(null);
    }

    public GuiDimension(String dimension){
        String[] components = dimension.split(",");
        if(components.length >= 4){
            mAmountPixel = Float.parseFloat(components[0]);
            mAmountCentiMeter = Float.parseFloat(components[1]);
            mParentWidthFactor = Float.parseFloat(components[2]);
            mParentHeightFactor = Float.parseFloat(components[3]);
        }
    }

    public void incrementPixel(float amount){
        mAmountPixel += amount;
    }

    public float getPixelDimension(){
        return mCachedValue;
    }

    public void invalidate(GuiElement parent){
        mCachedValue = mAmountPixel + mAmountCentiMeter * GuiManager.mCentiMeterToPixelRatio;
        if(parent != null){
            mCachedValue += mParentWidthFactor * 0.01f * parent.getRegion().getW() + mParentHeightFactor * 0.01f * parent.getRegion().getH();
        }
    }

    public static GuiDimension CentiMeter(float cm) {
        return new GuiDimension(0, cm);
    }
}
