package com.samsung.retailexperience.retailgrace.gson.model;

import android.util.Log;

import com.tecace.app.manager.gson.model.ArgumentsModel;

import java.io.Serializable;

/**
 * Created by icanmobile on 6/14/16.
 */
public class ArgsModel extends ArgumentsModel implements Serializable {
    private static final String TAG = ArgsModel.class.getSimpleName();

    public String legalGoToMainDemoAction;
    public String legalGoToSubDemoAction;

    public ArgsModel() {
        this(null, null);
    }

    public ArgsModel(String legalGoToMainDemoAction,
                            String legalGoToSubDemoAction) {
        this.legalGoToMainDemoAction = legalGoToMainDemoAction;
        this.legalGoToSubDemoAction = legalGoToSubDemoAction;
    }

    public String getLegalGoToMainDemoAction() {
        return this.legalGoToMainDemoAction;
    }
    public void setLegalGoToMainDemoAction(String legalGoToMainDemoAction) {
        this.legalGoToSubDemoAction = legalGoToMainDemoAction;
    }

    public String getLegalGoToSubDemoAction() {
        return this.legalGoToSubDemoAction;
    }
    public void setLegalGoToSubDemoAction(String legalGoToSubDemoAction) {
        this.legalGoToSubDemoAction = legalGoToSubDemoAction;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.legalGoToMainDemoAction != null)
            appendString(builder, "legalGoToMainDemoAction = " + this.legalGoToMainDemoAction);
        if (this.legalGoToSubDemoAction != null)
            appendString(builder, "legalGoToSubDemoAction = " + this.legalGoToSubDemoAction);

        return builder.toString();
    }

    @Override
    public void print() {
        Log.d(ArgsModel.class.getName(), toString());
    }
}
