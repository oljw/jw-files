package com.tecace.retail.videomanager.util;

import android.os.AsyncTask;

import com.tecace.retail.videomanager.gson.model.Chapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smheo on 9/29/2015.
 */
public class ChapterParseTask extends AsyncTask<String, Void, List<Chapter>> {

    public interface ChapterParseListener{
        void onChaptersParsed(List<Chapter> chapters);
    }

    private final ChapterParseUtil parseUtil;
    private ChapterParseListener listener;

    public ChapterParseTask(ChapterParseUtil parseUtil) {
        super();
        this.parseUtil = parseUtil;
    }

    public ChapterParseTask setListener(ChapterParseListener listener){
        this.listener = listener;
        return this;
    }

    @Override
    protected List<Chapter> doInBackground(String... params) {
        try {
            return parseUtil.getChapters(params[0]);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    protected void onPostExecute(List<Chapter> result) {
        super.onPostExecute(result);
        listener.onChaptersParsed(result);
    }
}
