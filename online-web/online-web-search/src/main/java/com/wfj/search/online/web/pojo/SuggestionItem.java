package com.wfj.search.online.web.pojo;

/**
 * <p>create at 16-1-23</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class SuggestionItem {
    public boolean history;
    public long count;
    public String text;

    @SuppressWarnings("unused")
    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    @SuppressWarnings("unused")
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @SuppressWarnings("unused")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
