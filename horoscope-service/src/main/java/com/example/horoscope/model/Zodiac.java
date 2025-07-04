package com.example.horoscope.model;

public enum Zodiac {
    ARIES("牡羊座", "おひつじ座", 3, 21, 4, 19),
    TAURUS("牡牛座", "おうし座", 4, 20, 5, 20),
    GEMINI("双子座", "ふたご座", 5, 21, 6, 21),
    CANCER("蟹座", "かに座", 6, 22, 7, 22),
    LEO("獅子座", "しし座", 7, 23, 8, 22),
    VIRGO("乙女座", "おとめ座", 8, 23, 9, 22),
    LIBRA("天秤座", "てんびん座", 9, 23, 10, 23),
    SCORPIO("蠍座", "さそり座", 10, 24, 11, 22),
    SAGITTARIUS("射手座", "いて座", 11, 23, 12, 21),
    CAPRICORN("山羊座", "やぎ座", 12, 22, 1, 19),
    AQUARIUS("水瓶座", "みずがめ座", 1, 20, 2, 18),
    PISCES("魚座", "うお座", 2, 19, 3, 20);

    private final String kanjiName;
    private final String hiraganaName;
    private final int startMonth;
    private final int startDay;
    private final int endMonth;
    private final int endDay;

    Zodiac(String kanjiName, String hiraganaName, int startMonth, int startDay, int endMonth, int endDay) {
        this.kanjiName = kanjiName;
        this.hiraganaName = hiraganaName;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endMonth = endMonth;
        this.endDay = endDay;
    }

    public String getKanjiName() {
        return kanjiName;
    }

    public String getHiraganaName() {
        return hiraganaName;
    }

    public String getDisplayName() {
        return kanjiName + "（" + hiraganaName + "）";
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public int getEndDay() {
        return endDay;
    }
}