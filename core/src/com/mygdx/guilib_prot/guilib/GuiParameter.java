package com.mygdx.guilib_prot.guilib;


public class GuiParameter {
    protected String mValueString;

    public GuiParameter(){
        mValueString = null;
    }

    public GuiParameter(String value) {
        mValueString = value;
    }

    public void setValueString(String value){ mValueString = value; }

    public String valueString() {
        return mValueString;
    }

    public boolean isValueSet() {
        return mValueString != null;
    }
}
