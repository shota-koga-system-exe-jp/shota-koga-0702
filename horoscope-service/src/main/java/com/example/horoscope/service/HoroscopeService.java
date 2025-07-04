package com.example.horoscope.service;

import com.example.horoscope.model.HoroscopeResult;
import com.example.horoscope.model.Zodiac;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HoroscopeService {

    private static final String[] FORTUNES = {
        "素晴らしい一日になるでしょう！新しいチャンスが訪れ、あなたの才能が輝きます。",
        "今日は絶好調！何をやってもうまくいく日です。積極的に行動しましょう。",
        "良い出会いがありそうです。人との繋がりを大切にすることで、幸運が舞い込みます。",
        "努力が実を結ぶ日です。これまでの頑張りが認められるでしょう。",
        "穏やかで充実した一日になります。心に余裕を持って過ごしましょう。",
        "小さな幸せがたくさん見つかる日です。日常の中に喜びを見出しましょう。",
        "バランスの取れた一日です。仕事もプライベートも順調に進みます。",
        "新しいアイデアが浮かびやすい日です。創造力を活かしてみましょう。",
        "コミュニケーションが円滑に進む日です。大切な話し合いには最適です。",
        "少し慎重に行動した方が良い日です。焦らずゆっくり進みましょう。",
        "体調管理に気をつけましょう。無理をせず、休息を大切にしてください。",
        "今日は充電の日です。明日への準備をしっかりと整えましょう。"
    };

    private static final String[] LUCKY_ITEMS = {
        "青いペン", "白いハンカチ", "観葉植物", "コーヒー", "音楽プレーヤー",
        "本", "時計", "香水", "チョコレート", "スマートフォン",
        "財布", "鍵", "メガネ", "ノート", "傘",
        "マスク", "ミントガム", "水筒", "イヤホン", "手帳",
        "ティッシュ", "リップクリーム", "ハンドクリーム", "カメラ", "お守り"
    };

    private static final String[] LUCKY_COLORS = {
        "赤", "青", "黄色", "緑", "紫", "オレンジ", "ピンク", "白", "黒", "茶色", "金色", "銀色"
    };

    public List<HoroscopeResult> getDailyHoroscope() {
        LocalDate today = LocalDate.now();
        Random random = new Random(today.toEpochDay());
        
        List<HoroscopeResult> results = new ArrayList<>();
        List<Zodiac> zodiacs = Arrays.asList(Zodiac.values());
        
        // シャッフルして順位を決定
        Collections.shuffle(zodiacs, random);
        
        for (int i = 0; i < zodiacs.size(); i++) {
            Zodiac zodiac = zodiacs.get(i);
            int ranking = i + 1;
            
            // ランキングに基づいて運勢を選択
            String fortune = FORTUNES[i];
            
            // ランダムにラッキーアイテムを選択
            String luckyItem = LUCKY_ITEMS[random.nextInt(LUCKY_ITEMS.length)];
            
            // ラッキーナンバー（1-9）
            int luckyNumber = random.nextInt(9) + 1;
            
            // ラッキーカラー
            String luckyColor = LUCKY_COLORS[random.nextInt(LUCKY_COLORS.length)];
            
            HoroscopeResult result = new HoroscopeResult(zodiac, ranking, fortune, luckyItem, luckyNumber, luckyColor);
            results.add(result);
        }
        
        // ランキング順にソート
        return results.stream()
                .sorted(Comparator.comparingInt(HoroscopeResult::getRanking))
                .collect(Collectors.toList());
    }
    
    public HoroscopeResult getHoroscopeByZodiac(Zodiac zodiac) {
        List<HoroscopeResult> allResults = getDailyHoroscope();
        return allResults.stream()
                .filter(result -> result.getZodiac() == zodiac)
                .findFirst()
                .orElse(null);
    }
}