package com.example.translator;

public class HistoryInfo {
    private int history_id;
    private String src;
    private String dst;
    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
    public HistoryInfo(int history_id, String src, String dst) {
        this.history_id = history_id;
        this.src = src;
        this.dst = dst;
    }
}
