package Dao;

public class Lrc {
    public String lrcText;
    public int textColor;
    public int fontSize;
    public int time;

    public Lrc() {
    }

    public Lrc(String lrcText, int textColor, int fontSize, int time) {
        this.lrcText = lrcText;
        this.textColor = textColor;
        this.fontSize = fontSize;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getLrcText() {
        return lrcText;
    }

    public void setLrcText(String lrcText) {
        this.lrcText = lrcText;
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