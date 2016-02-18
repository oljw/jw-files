package com.samsung.retailexperience.retailhero.util;

import android.util.Log;

/**
 * Created by jaekim on 2/1/16.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static final int NO_PHOTO_RESOURCE_INDEX = -1;

    public static int getTappedImageIndex(float touchX, float touchY, float marginLeft, float marginTop,
                                           float tapImageWidth, float tapImageHeight, int numColumns, int numRows) {
        int tappedImageIndex = NO_PHOTO_RESOURCE_INDEX;
        if (touchX > marginLeft && touchY > marginTop) {
            int columnIndex = (int) ((touchX - marginLeft) / tapImageWidth);
            int rowIndex = (int) ((touchY - marginTop) / tapImageHeight);
            if (columnIndex < numColumns && rowIndex < numRows) {
                tappedImageIndex = rowIndex * numColumns + columnIndex;
                Log.i(TAG, "row: " + (rowIndex) + ", col: " + (columnIndex) + "is selected index: " +
                        tappedImageIndex);
            }
        }
        return tappedImageIndex;
    }

    public static class TappedImageIndexHelper {
        float touchX, touchY, marginLeft, marginTop, tapImageWidth, tapImageHeight;
        int numColumns, numRows;
        boolean hasMargin, hasSize, hasColRows;
        public TappedImageIndexHelper(float touchX, float touchY) {
            this.touchX = touchX;
            this.touchY = touchY;
        }

        public TappedImageIndexHelper setMargin(float left, float top) {
            this.marginLeft = left;
            this.marginTop = top;
            hasMargin = true;
            return this;
        }

        public TappedImageIndexHelper setImageSize(float width, float height) {
            this.tapImageWidth = width;
            this.tapImageHeight = height;
            hasSize = true;
            return this;
        }
        public TappedImageIndexHelper setColumnsRows(int columns, int rows) {
            this.numColumns = columns;
            this.numRows = rows;
            hasColRows = true;
            return this;
        }

        public int getTappedImageIndex() {
            if (!hasMargin || !hasSize || !hasColRows) {
                throw new AssertionError("hasMargin: " + hasMargin + ", hasSize: " + hasSize + ", hasColRows: " + hasColRows);
            }
            int tappedImageIndex = NO_PHOTO_RESOURCE_INDEX;
            if (touchX > marginLeft && touchY > marginTop) {
                int columnIndex = (int) ((touchX - marginLeft) / tapImageWidth);
                int rowIndex = (int) ((touchY - marginTop) / tapImageHeight);
                if (columnIndex < numColumns && rowIndex < numRows) {
                    tappedImageIndex = rowIndex * numColumns + columnIndex;
                    Log.i(TAG, "row: " + (rowIndex) + ", col: " + (columnIndex) + "is selected index: " +
                            tappedImageIndex);
                }
            }
            return tappedImageIndex;
        }
    }

}
