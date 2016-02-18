package com.samsung.retailexperience.retailhero.util;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samsung.retailexperience.retailhero.gson.models.Chapter;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by smheo on 9/29/2015.
 */
public class ChapterParseUtil {
    private static final String TAG = "ChapterParseUtil";

    private final Resources resources;

    public ChapterParseUtil(Resources resources) {
        this.resources = resources;
    }

    public List<Chapter> getChapters(String filepath) throws SAXException, IOException {
        Gson gson = new Gson();

        File file = new File(filepath);
        if (!file.exists()) {
            Log.e(TAG, "no chapter file : " + filepath);
            return null;
        }

        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        Type collectionType = new TypeToken<List<Chapter>>(){}.getType();
        List<Chapter> chapters = gson.fromJson(reader, collectionType);

        Collections.sort(chapters, new ChapterComparitor());

        if (reader != null)
            reader.close();

        return chapters;
    }

    private class ChapterComparitor implements Comparator<Chapter> {
        @Override
        public int compare(Chapter lhs, Chapter rhs) {
            return lhs.getChapterStart() - rhs.getChapterStart();
        }
    }
}
