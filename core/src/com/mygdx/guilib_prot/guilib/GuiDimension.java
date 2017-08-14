package com.mygdx.guilib_prot.guilib;

/**
 * Created by ovreb on 31.05.2016.
 */
public class GuiDimension extends GuiParameter {
    private float   mAmountPixel;
    private float   mAmountMilliMeter;
    private float   mParentWidthFactor;
    private float   mParentHeightFactor;

    private float   mCachedValue;

    public GuiDimension(){
        mAmountPixel = 0.0f;
        mAmountMilliMeter = 0.0f;
        mParentWidthFactor = 0.0f;
        mParentHeightFactor = 0.0f;
        invalidate(null);
    }

    public GuiDimension(float amPixel) {
        mAmountPixel = amPixel;
        mAmountMilliMeter = 0.0f;
        mParentWidthFactor = 0.0f;
        mParentHeightFactor = 0.0f;
        invalidate(null);
    }

    public GuiDimension(float amPixel, float amDIPixel){
        mAmountPixel = amPixel;
        mAmountMilliMeter = amDIPixel;
        mParentWidthFactor = 0.0f;
        mParentHeightFactor = 0.0f;
        invalidate(null);
    }

    public GuiDimension(float amPixel, float amDIPixel, float parentWidthFactor, float parentHeightFactor){
        mAmountPixel = amPixel;
        mAmountMilliMeter = amDIPixel;
        mParentWidthFactor = parentWidthFactor;
        mParentHeightFactor = parentHeightFactor;
        invalidate(null);
    }

    public GuiDimension(String dimension){
        super(dimension);
        if(dimension != null) {
            String[] components = dimension.split(",");
            if (components.length > 0) {
                for(String component : components){
                    if(component.toLowerCase().endsWith("px")){
                        mAmountPixel += Float.parseFloat(component.substring(0, component.length()-2));
                    }
                    else if(component.toLowerCase().endsWith("mm")){
                        mAmountMilliMeter += Float.parseFloat(component.substring(0, component.length()-2));
                    }
                    else if(component.toLowerCase().endsWith("cm")){
                        mAmountMilliMeter += Float.parseFloat(component.substring(0, component.length()-2)) / 10.0f;
                    }
                    else if(component.toLowerCase().endsWith("%pw")){
                        mParentWidthFactor += Float.parseFloat(component.substring(0, component.length()-3));
                    }
                    else if(component.toLowerCase().endsWith("%ph")){
                        mParentHeightFactor += Float.parseFloat(component.substring(0, component.length()-3));
                    }
                    // If there is no match, assume there is no prefix and default to pixel coordinates
                    else {
                        mAmountPixel += Float.parseFloat(component);
                    }
                }
            }
        }
        else {
            mAmountPixel = 0.0f;
            mAmountMilliMeter = 0.0f;
            mParentWidthFactor = 0.0f;
            mParentHeightFactor = 0.0f;
        }
        invalidate(null);
    }

    // Copy constructor
    public GuiDimension(GuiDimension clone){
        super(clone.mValueString);
        mAmountPixel = clone.mAmountPixel;
        mAmountMilliMeter = clone.mAmountMilliMeter;
        mParentWidthFactor = clone.mParentWidthFactor;
        mParentHeightFactor = clone.mParentHeightFactor;
        mCachedValue = clone.mCachedValue;
    }

    public void add(GuiDimension operand){
        mAmountPixel += operand.mAmountPixel;
        mAmountMilliMeter += operand.mAmountMilliMeter;
        mParentWidthFactor += operand.mParentWidthFactor;
        mParentHeightFactor += operand.mParentHeightFactor;
    }

    public void incrementPixel(float amount){
        mAmountPixel += amount;
    }

    public float getPixelDimension(){
        return mCachedValue;
    }

    public void invalidate(GuiElement parent){
        mCachedValue = mAmountPixel + mAmountMilliMeter * GuiManager.mMilliMeterToPixelRatio;
        if(parent != null){
            mCachedValue += mParentWidthFactor * 0.01f * parent.getRegion().getW() + mParentHeightFactor * 0.01f * parent.getRegion().getH();
        }
    }

    public static GuiDimension CentiMeter(float cm) {
        return new GuiDimension(0, cm);
    }
}
