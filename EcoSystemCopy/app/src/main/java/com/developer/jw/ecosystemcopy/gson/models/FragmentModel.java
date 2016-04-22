package com.developer.jw.ecosystemcopy.gson.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icanmobile on 3/1/16.
 */
public class FragmentModel <T> extends BaseModel implements Serializable {
    private String me;
    private String name;
    private String sibling;
    private String className;
    private String layout;
    private String background;
    private String imageBG;
    private ArrayList<String> imageBGMargin = new ArrayList<String>();
    private String imageBGAlign;
    private String action;
    private String actionBackKey;
    private String forwardEnterAnim;
    private String forwardExitAnim;
    private String backwardEnterAnim;
    private String backwardExitAnim;
    private String addEnterAnim;
    private String pivotX;
    private String pivotY;
    private String reservedData;
    private T fragment;

    public FragmentModel() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
    public FragmentModel(String me,
                         String name,
                         String sibling,
                         String className,
                         String layout,
                         String background,
                         String imageBG,
                         ArrayList<String> imageBGMargin,
                         String imageBGAlign,
                         String action,
                         String actionBackKey,
                         String forwardEnterAnim,
                         String forwardExitAnim,
                         String backwardEnterAnim,
                         String backwardExitAnim,
                         String addEnterAnim,
                         String pivotX,
                         String pivotY,
                         T      fragment,
                         String reservedData) {
        this.me                 = me;
        this.name               = name;
        this.sibling            = sibling;
        this.className          = className;
        this.layout             = layout;
        this.background         = background;
        this.imageBG            = imageBG;
        this.imageBGMargin      = imageBGMargin;
        this.imageBGAlign       = imageBGAlign;
        this.action             = action;
        this.actionBackKey      = actionBackKey;
        this.forwardEnterAnim   = forwardEnterAnim;
        this.forwardExitAnim    = forwardExitAnim;
        this.backwardEnterAnim  = backwardEnterAnim;
        this.backwardExitAnim   = backwardExitAnim;
        this.addEnterAnim       = addEnterAnim;
        this.pivotX             = pivotX;
        this.pivotY             = pivotY;
        this.fragment           = fragment;
        this.reservedData       = reservedData;
    }

    // current ui state
    public String getMe() {
        return this.me;
    }
    public void setMe(String me) {
        this.me = me;
    }

    // my name
    public String getName() {
        return this.name;
    }
    public int getNameResId() {
        if (this.name != null)
            return getResId(this.name);
        return 0;
    }
    public void setName(String name) {
        this.name = name;
    }

    // sibling ui state for legal page
    public String getSibling() {
        return this.sibling;
    }
    public void setSibling(String sibling) {
        this.sibling = sibling;
    }

    // class Name
    public String getClassName() {
        return this.className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    // layout resource id
    public String getLayout() {
        return this.layout;
    }
    public int getLayoutResId() {
        if (this.layout != null)
            return getResId(this.layout);
        return 0;
    }
    public void setLayout(String layout) {
        this.layout = layout;
    }

    // next fragment json file
    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    // previous fragment json file when user clicks back key
    public String getActionBackKey() {
        return this.actionBackKey;
    }
    public void setActionBackKey(String actionBackKey) {
        this.actionBackKey = actionBackKey;
    }

    // background resource id
    public String getBackground() {
        return this.background;
    }
    public int getBackgroundResId() {
        if (this.background != null)
            return getResId(this.background);
        return 0;
    }
    public void setBackground(String background) {
        this.background = background;
    }

    // image background resource id
    public String getImageBG() {
        return this.imageBG;
    }
    public int getImageBGResId() {
        if (this.imageBG != null)
            return getResId(this.imageBG);
        return 0;
    }
    public void setImageBG(String imageBG) {
        this.imageBG = imageBG;
    }

    // image background margin
    public ArrayList<String> getImageBGMargin() {
        return this.imageBGMargin;
    }
    public void setImageBGMargin(ArrayList<String> imageBGMargin) {
        this.imageBGMargin = imageBGMargin;
    }
    public int getImageBGMarginLeftResId() {
        if (this.imageBGMargin == null) return 0;
        if (this.imageBGMargin.get(0) != null)
            return getResId(this.imageBGMargin.get(0));
        return 0;
    }
    public int getImageBGMarginTopResId() {
        if (this.imageBGMargin == null) return 0;
        if (this.imageBGMargin.get(1) != null)
            return getResId(this.imageBGMargin.get(1));
        return 0;
    }
    public int getImageBGMarginRightResId() {
        if (this.imageBGMargin == null) return 0;
        if (this.imageBGMargin.get(2) != null)
            return getResId(this.imageBGMargin.get(2));
        return 0;
    }
    public int getImageBGMarginBottomResId() {
        if (this.imageBGMargin == null) return 0;
        if (this.imageBGMargin.get(3) != null)
            return getResId(this.imageBGMargin.get(3));
        return 0;
    }

    // image Align
    public String getImageBGAlign() {
        return this.imageBGAlign;
    }
    public int getImageBGAlignResId() {
        if (this.imageBGAlign != null)
            return getResId(this.imageBGAlign);
        return 0;
    }
    public void setImageBGAlign(String imageBGAlign) {
        this.imageBGAlign = imageBGAlign;
    }


    // forward enter fragment transition
    public String getForwardEnterAnim() {
        return this.forwardEnterAnim;
    }
    public int getForwardEnterAnimResId() {
        if (this.forwardEnterAnim != null)
            return getResId(this.forwardEnterAnim);
        return 0;
    }
    public void setForwardEnterAnim(String forwardEnterAnim) {
        this.forwardEnterAnim = forwardEnterAnim;
    }

    // forward exit fragment transition
    public String getForwardExitAnim() {
        return this.forwardExitAnim;
    }
    public int getForwardExitAnimResId() {
        if (this.forwardExitAnim != null)
            return getResId(this.forwardExitAnim);
        return 0;
    }
    public void setForwardExitAnim(String forwardExitAnim) {
        this.forwardExitAnim = forwardExitAnim;
    }

    // backward enter fragment transition
    public String getBackwardEnterAnim() {
        return this.backwardEnterAnim;
    }
    public int getBackwardEnterAnimResId() {
        if (this.backwardEnterAnim != null)
            return getResId(this.backwardEnterAnim);
        return 0;
    }
    public void setBackwardEnterAnim(String backwardEnterAnim) {
        this.backwardEnterAnim = backwardEnterAnim;
    }

    // backward exit fragment transition
    public String getBackwardExitAnim() {
        return this.backwardExitAnim;
    }
    public int getBackwardExitAnimResId() {
        if (this.backwardEnterAnim != null)
            return getResId(this.backwardExitAnim);
        return 0;
    }
    public void setBackwardExitAnim(String backwardExitAnim) {
        this.backwardExitAnim = backwardExitAnim;
    }

    // add enter fragment transition
    public String getAddEnterAnim() {
        return this.addEnterAnim;
    }
    public int getAddEnterAnimResId() {
        if (this.addEnterAnim != null)
            return getResId(this.addEnterAnim);
        return 0;
    }
    public void setAddEnterAnim(String addEnterAnim) {
        this.addEnterAnim = addEnterAnim;
    }

    // view pivot X, Y values for fragment transition
    public String getPivotX() {
        return this.pivotX;
    }
    public void setPivotX(String pivotX) {
        this.pivotX = pivotX;
    }
    public int getPivotXValue() {
        if (this.pivotX == null || this.pivotX.equals("0")) return 0;
        return getResId(this.pivotX);
    }
    public String getPivotY() {
        return this.pivotY;
    }
    public void setPivotY(String pivotY) {
        this.pivotY = pivotY;
    }
    public int getPivotYValue() {
        if (this.pivotY == null || this.pivotY.equals("0")) return 0;
        return getResId(this.pivotY);
    }

    // specific fragment information using json format
    public T getFragment() {
        return this.fragment;
    }
    public void setFragment(T fragment) {
        this.fragment = fragment;
    }

    // resolved data
    public String getReservedData() {
        return this.reservedData;
    }
    public int getReservedDataResId() {
        if (this.reservedData != null)
            return getResId(this.reservedData);
        return 0;
    }
    public void setReservedData(String reservedData) {
        this.reservedData = reservedData;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.me != null)
            appendString(builder, "me = " + this.me);
        if (this.name != null)
            appendString(builder, "name = " + this.name);
        if (this.layout != null)
            appendString(builder, "layout = " + this.layout);
        if (this.action != null)
            appendString(builder, "action = " + this.action);
        if (this.actionBackKey != null)
            appendString(builder, "actionBackKey = " + this.actionBackKey);
        if (this.background != null)
            appendString(builder, "background = " + this.background);
        if (this.imageBG != null)
            appendString(builder, "imageBG = " + this.imageBG);
        if (this.imageBGMargin != null) {
            for (int i = 0; i < this.imageBGMargin.size(); i++)
                appendString(builder, "imageBGMargin[" + i + "] = " + this.imageBGMargin.get(i));
        }
        if (this.forwardEnterAnim != null)
            appendString(builder, "forwardEnterAnim = " + this.forwardEnterAnim);
        if (this.forwardExitAnim != null)
            appendString(builder, "forwardExitAnim = " + this.forwardExitAnim);
        if (this.backwardEnterAnim != null)
            appendString(builder, "backwardEnterAnim = " + this.backwardEnterAnim);
        if (this.backwardExitAnim != null)
            appendString(builder, "backwardExitAnim = " + this.backwardExitAnim);
        if (this.addEnterAnim != null)
            appendString(builder, "addEnterAnim = " + this.addEnterAnim);
        if (this.pivotX != null)
            appendString(builder, "pivotX = " + this.pivotX);
        if (this.pivotY != null)
            appendString(builder, "pivotY = " + this.pivotY);
        if (this.fragment != null) {
            appendString(builder, "fragment {");
            appendString(builder, this.fragment.toString());
            appendString(builder, "}");
        }
        if (this.reservedData != null)
            appendString(builder, "reservedData = " + this.reservedData);

        return builder.toString();
    }

    @Override
    public void print() {
        Log.d(FragmentModel.class.getName(), toString());
    }
}
