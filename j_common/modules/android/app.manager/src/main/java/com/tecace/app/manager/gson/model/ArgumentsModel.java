package com.tecace.app.manager.gson.model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by icanmobile on 3/30/16.
 */
public class ArgumentsModel extends BaseModel implements Serializable {
    private static final String TAG = ArgumentsModel.class.getSimpleName();

    public String fragmentJson;
    public String transitionDir;

    public ArgumentsModel() {
        this(null, null);
    }

    public ArgumentsModel(String fragmentJson,
                          String transactionDir) {
        this.fragmentJson = fragmentJson;
        this.transitionDir = transactionDir;
    }

    public String getFragmentJson() {
        return this.fragmentJson;
    }
    public void setFragmentJson(String fragmentJson) {
        this.fragmentJson = fragmentJson;
    }

    public String getTransitionDir() {
        return this.transitionDir;
    }
    public void setTransitionDir(String transitionDir) {
        this.transitionDir = transitionDir;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.fragmentJson != null)
            appendString(builder, "fragmentJson = " + this.fragmentJson);
        if (this.transitionDir != null)
            appendString(builder, "transitionDir = " + this.transitionDir);

        return builder.toString();
    }

    @Override
    public void print() {
        Log.d(ArgumentsModel.class.getName(), toString());
    }
}
