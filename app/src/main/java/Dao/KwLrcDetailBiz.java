package Dao;

public class KwLrcDetailBiz {
    private String lineLyric;
    private int textColor;
    private int fontSize;
    private String time;

    public KwLrcDetailBiz(String lineLyric, int textColor, int fontSize, String time) {
        this.lineLyric = lineLyric;
        this.textColor = textColor;
        this.fontSize = fontSize;
        this.time = time;
    }

    public String getLineLyric() {
        return lineLyric;
    }

    public void setLineLyric(String lineLyric) {
        this.lineLyric = lineLyric;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
