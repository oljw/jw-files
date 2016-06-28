package com.tecace.retail.videomanager.util;

import android.os.AsyncTask;

import com.tecace.retail.videomanager.models.ExtendedChapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smheo on 9/29/2015.
 */
public class ChapterParseTask extends AsyncTask<String, Void, List<ExtendedChapter>> {

    public interface ChapterParseListener{
        void onChaptersParsed(List<ExtendedChapter> chapters);
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
    protected List<ExtendedChapter> doInBackground(String... params) {
        try {
            return parseUtil.getChapters(params[0]);
        } catch (Exception e) {
            return new ArrayList<ExtendedChapter>();
        }
    }

    @Override
    protected void onPostExecute(List<ExtendedChapter> result) {
        super.onPostExecute(result);
        listener.onChaptersParsed(result);
    }
}
