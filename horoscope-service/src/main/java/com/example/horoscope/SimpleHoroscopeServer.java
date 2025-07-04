package com.example.horoscope;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SimpleHoroscopeServer {
    
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
        "マスク", "ミントガム", "水筒", "イヤホン", "手帳"
    };
    
    private static final String[] ZODIAC_SIGNS = {
        "牡羊座（おひつじ座）", "牡牛座（おうし座）", "双子座（ふたご座）",
        "蟹座（かに座）", "獅子座（しし座）", "乙女座（おとめ座）",
        "天秤座（てんびん座）", "蠍座（さそり座）", "射手座（いて座）",
        "山羊座（やぎ座）", "水瓶座（みずがめ座）", "魚座（うお座）"
    };
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HoroscopeHandler());
        server.setExecutor(null);
        System.out.println("Horoscope server is running on http://localhost:8080");
        server.start();
    }
    
    static class HoroscopeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = generateHTML();
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("UTF-8"));
            os.close();
        }
        
        private String generateHTML() {
            Random random = new Random();
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"ja\">\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            html.append("    <title>今日の占い</title>\n");
            html.append("    <style>\n");
            html.append("        body {\n");
            html.append("            font-family: 'Hiragino Sans', 'Meiryo', sans-serif;\n");
            html.append("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n");
            html.append("            margin: 0;\n");
            html.append("            padding: 20px;\n");
            html.append("            min-height: 100vh;\n");
            html.append("            display: flex;\n");
            html.append("            justify-content: center;\n");
            html.append("            align-items: center;\n");
            html.append("        }\n");
            html.append("        .container {\n");
            html.append("            background: rgba(255, 255, 255, 0.95);\n");
            html.append("            border-radius: 20px;\n");
            html.append("            padding: 40px;\n");
            html.append("            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);\n");
            html.append("            max-width: 800px;\n");
            html.append("            width: 100%;\n");
            html.append("        }\n");
            html.append("        h1 {\n");
            html.append("            color: #5a67d8;\n");
            html.append("            text-align: center;\n");
            html.append("            margin-bottom: 30px;\n");
            html.append("            font-size: 2.5em;\n");
            html.append("        }\n");
            html.append("        .date {\n");
            html.append("            text-align: center;\n");
            html.append("            color: #718096;\n");
            html.append("            font-size: 1.2em;\n");
            html.append("            margin-bottom: 30px;\n");
            html.append("        }\n");
            html.append("        .zodiac-grid {\n");
            html.append("            display: grid;\n");
            html.append("            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));\n");
            html.append("            gap: 20px;\n");
            html.append("            margin-bottom: 30px;\n");
            html.append("        }\n");
            html.append("        .zodiac-card {\n");
            html.append("            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);\n");
            html.append("            color: white;\n");
            html.append("            padding: 20px;\n");
            html.append("            border-radius: 15px;\n");
            html.append("            text-align: center;\n");
            html.append("            transition: transform 0.3s ease;\n");
            html.append("            cursor: pointer;\n");
            html.append("        }\n");
            html.append("        .zodiac-card:hover {\n");
            html.append("            transform: translateY(-5px);\n");
            html.append("        }\n");
            html.append("        .fortune-section {\n");
            html.append("            background: #f7fafc;\n");
            html.append("            padding: 30px;\n");
            html.append("            border-radius: 15px;\n");
            html.append("            margin-top: 30px;\n");
            html.append("        }\n");
            html.append("        .fortune-title {\n");
            html.append("            color: #5a67d8;\n");
            html.append("            font-size: 1.5em;\n");
            html.append("            margin-bottom: 15px;\n");
            html.append("        }\n");
            html.append("        .fortune-text {\n");
            html.append("            color: #4a5568;\n");
            html.append("            line-height: 1.8;\n");
            html.append("            font-size: 1.1em;\n");
            html.append("        }\n");
            html.append("        .lucky-item {\n");
            html.append("            background: #e6fffa;\n");
            html.append("            color: #234e52;\n");
            html.append("            padding: 10px 20px;\n");
            html.append("            border-radius: 25px;\n");
            html.append("            display: inline-block;\n");
            html.append("            margin-top: 20px;\n");
            html.append("            font-weight: bold;\n");
            html.append("        }\n");
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <div class=\"container\">\n");
            html.append("        <h1>✨ 今日の占い ✨</h1>\n");
            html.append("        <div class=\"date\">").append(today).append("</div>\n");
            html.append("        \n");
            html.append("        <div class=\"zodiac-grid\">\n");
            
            for (String zodiac : ZODIAC_SIGNS) {
                html.append("            <div class=\"zodiac-card\">\n");
                html.append("                ").append(zodiac).append("\n");
                html.append("            </div>\n");
            }
            
            html.append("        </div>\n");
            html.append("        \n");
            html.append("        <div class=\"fortune-section\">\n");
            html.append("            <div class=\"fortune-title\">🔮 本日の運勢</div>\n");
            html.append("            <div class=\"fortune-text\">\n");
            html.append("                ").append(FORTUNES[random.nextInt(FORTUNES.length)]).append("\n");
            html.append("            </div>\n");
            html.append("            <div class=\"lucky-item\">\n");
            html.append("                🍀 ラッキーアイテム: ").append(LUCKY_ITEMS[random.nextInt(LUCKY_ITEMS.length)]).append("\n");
            html.append("            </div>\n");
            html.append("        </div>\n");
            html.append("    </div>\n");
            html.append("</body>\n");
            html.append("</html>\n");
            
            return html.toString();
        }
    }
}