package com.tecace.retail.videomanager.gson.model;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.ColorInt;

/**
 * Created by jaekim on 6/8/16.
 */
public class InteractionOverlay {
    private final int leftPadding;
    private final int topPadding;
    private final String overlayImage;
    private final Rect overlayImageRect;
    private final String bgColorValue;
    private final Rect touchableArea;

    public InteractionOverlay(int leftPadding, int topPadding,
                              String overlayImage, Rect overlayImageRect,
                              String bgColorValue,
                              Rect touchableArea) {
        this.leftPadding = leftPadding;
        this.topPadding = topPadding;
        this.overlayImage = overlayImage;
        this.overlayImageRect = overlayImageRect;
        this.bgColorValue = bgColorValue;
        this.touchableArea = touchableArea;
    }

    public int getLeftPadding() {
        return leftPadding;
    }
    public int getTopPadding() {
        return topPadding;
    }

    public String getOverlayImageName() {
        return overlayImage;
    }

    public Rect getOverlayImageRect() {
        return overlayImageRect;
    }

    public Rect getTouchableArea() {
        return touchableArea;
    }

    @ColorInt
    public int getBackgroundColor() {
        return Color.parseColor(bgColorValue);
    }

    @Override
    public String toString() {
        return String.format("Padding: [l:%d, t:%d]," +
                        "overlayImage:%s, BgColor:%s",
                leftPadding, topPadding,
                overlayImage, bgColorValue);
    }
}
