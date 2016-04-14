package com.developer.jw.criminalintent;

import java.util.UUID;

/**
 * Created by JW on 2016-04-13.
 */
public class Crime {
    private UUID mId;
    private String mTitle;

    public Crime() {
        //Generate Unique identifier
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
