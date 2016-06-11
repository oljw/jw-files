package com.tecace.retail.videomanager.util;

import com.google.gson.reflect.TypeToken;
import com.tecace.retail.appmanager.RetailApplication;
import com.tecace.retail.appmanager.util.JsonUtil;
import com.tecace.retail.videomanager.gson.model.Chapter;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by smheo on 9/29/2015.
 */
public class ChapterParseUtil {
    private static final String TAG = ChapterParseUtil.class.getSimpleName();

    public List<Chapter> getChapters(String filepath) throws SAXException, IOException {
        Type collectionType = new TypeToken<List<Chapter>>(){}.getType();
        List<Chapter> chapters = JsonUtil.getInstance().loadJsonModel(
                RetailApplication.getContext(), filepath, collectionType);
        Collections.sort(chapters, new ChapterComparitor());
        return chapters;
    }

    private class ChapterComparitor implements Comparator<Chapter> {
        @Override
        public int compare(Chapter lhs, Chapter rhs) {
            return lhs.getChapterStart() - rhs.getChapterStart();
        }
    }
}