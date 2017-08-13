package com.mygdx.guilib_prot.guilib;

public class GuiParameter {
    protected String mValue;
    protected boolean mValueSet;
    protected boolean mReferenceParameter;
    protected String mReferenceName;

    public GuiParameter() {
        mValue = "";
        mValueSet = false;
        mReferenceParameter = false;
    }

    public GuiParameter(String value) {
        mValue = value;
        mValueSet = true;
        mReferenceParameter = false;
    }

    public String valueString() {
        return mValue;
    }

    public boolean isValueSet() {
        return mValueSet;
    }
}
