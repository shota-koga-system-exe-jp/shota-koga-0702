package com.example.horoscope.model;

public class HoroscopeResult {
    private Zodiac zodiac;
    private int ranking;
    private String fortune;
    private String luckyItem;
    private int luckyNumber;
    private String luckyColor;

    public HoroscopeResult() {
    }

    public HoroscopeResult(Zodiac zodiac, int ranking, String fortune, String luckyItem, int luckyNumber, String luckyColor) {
        this.zodiac = zodiac;
        this.ranking = ranking;
        this.fortune = fortune;
        this.luckyItem = luckyItem;
        this.luckyNumber = luckyNumber;
        this.luckyColor = luckyColor;
    }

    public Zodiac getZodiac() {
        return zodiac;
    }

    public void setZodiac(Zodiac zodiac) {
        this.zodiac = zodiac;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getFortune() {
        return fortune;
    }

    public void setFortune(String fortune) {
        this.fortune = fortune;
    }

    public String getLuckyItem() {
        return luckyItem;
    }

    public void setLuckyItem(String luckyItem) {
        this.luckyItem = luckyItem;
    }

    public int getLuckyNumber() {
        return luckyNumber;
    }

    public void setLuckyNumber(int luckyNumber) {
        this.luckyNumber = luckyNumber;
    }

    public String getLuckyColor() {
        return luckyColor;
    }

    public void setLuckyColor(String luckyColor) {
        this.luckyColor = luckyColor;
    }
}