package com.example.horoscope;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.example.horoscope.model.Zodiac;
import com.example.horoscope.model.HoroscopeResult;
import com.example.horoscope.service.HoroscopeService;

public class StandaloneHoroscopeApp {
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HoroscopeHandler());
        server.createContext("/detail", new DetailHandler());
        server.setExecutor(null);
        System.out.println("星座占いサーバーが起動しました: http://localhost:8080");
        System.out.println("終了するには Ctrl+C を押してください");
        server.start();
    }

    static class HoroscopeHandler implements HttpHandler {
        private final HoroscopeService horoscopeService = new HoroscopeService();
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = generateHtml();
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }

        private String generateHtml() {
            List<HoroscopeResult> horoscopes = horoscopeService.getDailyHoroscope();
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
            String todayStr = today.format(formatter);

            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"ja\">\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            html.append("    <title>今日の星座占い</title>\n");
            html.append("    <style>\n");
            html.append("        body { font-family: 'Hiragino Sans', 'Meiryo', sans-serif; margin: 0; padding: 0; background: #000814; color: #e0e0e0; min-height: 100vh; position: relative; overflow-x: hidden; }\n");
            html.append("        #stars-container { position: fixed; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none; z-index: 0; }\n");
            html.append("        .star { position: absolute; background: white; border-radius: 50%; animation: twinkle 3s ease-in-out infinite; }\n");
            html.append("        .star.bright { box-shadow: 0 0 4px white; }\n");
            html.append("        .star.yellow { background: #ffd60a; box-shadow: 0 0 6px #ffd60a; }\n");
            html.append("        .star.blue { background: #74c0fc; box-shadow: 0 0 4px #74c0fc; }\n");
            html.append("        @keyframes twinkle { 0%, 100% { opacity: 0.3; transform: scale(1); } 50% { opacity: 1; transform: scale(1.2); } }\n");
            html.append("        @keyframes shooting { 0% { transform: translateX(0) translateY(0); opacity: 1; } 100% { transform: translateX(300px) translateY(300px); opacity: 0; } }\n");
            html.append("        .shooting-star { position: absolute; width: 2px; height: 2px; background: white; box-shadow: 0 0 6px 2px white; animation: shooting 1s ease-out; animation-fill-mode: forwards; }\n");
            html.append("        .shooting-star::after { content: ''; position: absolute; top: 0; left: -50px; width: 50px; height: 1px; background: linear-gradient(to left, white, transparent); }\n");
            html.append("        .milky-way { position: fixed; top: -50%; left: -50%; width: 200%; height: 200%; background: radial-gradient(ellipse at center, transparent 0%, transparent 30%, rgba(138, 43, 226, 0.05) 50%, rgba(30, 0, 50, 0.2) 100%); transform: rotate(-30deg); pointer-events: none; z-index: 0; }\n");
            html.append("        .nebula { position: fixed; width: 300px; height: 300px; border-radius: 50%; background: radial-gradient(circle at center, rgba(138, 43, 226, 0.1), transparent 70%); filter: blur(40px); pointer-events: none; z-index: 0; }\n");
            html.append("        .container { max-width: 1200px; margin: 0 auto; padding: 20px; position: relative; z-index: 2; }\n");
            html.append("        h1 { text-align: center; color: #ffd700; margin-bottom: 10px; font-size: 2.5em; text-shadow: 0 0 20px rgba(255, 215, 0, 0.5); }\n");
            html.append("        .date { text-align: center; color: #b8c5d6; margin-bottom: 30px; font-size: 1.2em; }\n");
            html.append("        .horoscope-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(350px, 1fr)); gap: 20px; margin-top: 30px; }\n");
            html.append("        .horoscope-card { background: rgba(255, 255, 255, 0.1); backdrop-filter: blur(10px); border: 1px solid rgba(255, 255, 255, 0.2); border-radius: 15px; padding: 20px; box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37); transition: all 0.3s ease; cursor: pointer; text-decoration: none; color: #e0e0e0; display: block; position: relative; overflow: hidden; }\n");
            html.append("        .horoscope-card::before { content: ''; position: absolute; top: -2px; left: -2px; right: -2px; bottom: -2px; background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1), transparent); transform: translateX(-100%); transition: transform 0.6s; }\n");
            html.append("        .horoscope-card:hover { transform: translateY(-5px); box-shadow: 0 12px 40px 0 rgba(31, 38, 135, 0.5); background: rgba(255, 255, 255, 0.15); }\n");
            html.append("        .horoscope-card:hover::before { transform: translateX(100%); }\n");
            html.append("        .zodiac-symbol { position: absolute; top: 10px; right: 10px; font-size: 3em; opacity: 0.3; color: #ffd700; }\n");
            html.append("        .ranking { display: inline-block; width: 50px; height: 50px; line-height: 50px; text-align: center; border-radius: 50%; font-weight: bold; font-size: 1.2em; margin-right: 10px; box-shadow: 0 0 10px rgba(255, 255, 255, 0.3); }\n");
            html.append("        .ranking-1 { background: linear-gradient(135deg, #FFD700, #FFA500); color: #333; box-shadow: 0 0 20px rgba(255, 215, 0, 0.6); }\n");
            html.append("        .ranking-2 { background: linear-gradient(135deg, #C0C0C0, #B8B8B8); color: #333; box-shadow: 0 0 20px rgba(192, 192, 192, 0.6); }\n");
            html.append("        .ranking-3 { background: linear-gradient(135deg, #CD7F32, #B87333); color: white; box-shadow: 0 0 20px rgba(205, 127, 50, 0.6); }\n");
            html.append("        .ranking-4, .ranking-5, .ranking-6 { background: linear-gradient(135deg, #3498db, #2980b9); color: white; }\n");
            html.append("        .ranking-7, .ranking-8, .ranking-9 { background: linear-gradient(135deg, #95a5a6, #7f8c8d); color: white; }\n");
            html.append("        .ranking-10, .ranking-11, .ranking-12 { background: linear-gradient(135deg, #e74c3c, #c0392b); color: white; }\n");
            html.append("        .zodiac-name { display: inline-block; font-size: 1.3em; font-weight: bold; color: #ffd700; margin-bottom: 15px; text-shadow: 0 0 10px rgba(255, 215, 0, 0.3); }\n");
            html.append("        .fortune { margin: 15px 0; line-height: 1.6; color: #d0d0d0; }\n");
            html.append("        .lucky-info { margin-top: 20px; padding-top: 15px; border-top: 1px solid rgba(255, 255, 255, 0.2); }\n");
            html.append("        .lucky-item { display: flex; align-items: center; margin: 8px 0; font-size: 0.95em; }\n");
            html.append("        .lucky-label { font-weight: bold; color: #a0a0a0; margin-right: 10px; min-width: 100px; }\n");
            html.append("        .lucky-value { color: #e0e0e0; }\n");
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <div id=\"stars-container\"></div>\n");
            html.append("    <div class=\"milky-way\"></div>\n");
            html.append("    <div class=\"nebula\" style=\"top: 20%; left: 70%;\"></div>\n");
            html.append("    <div class=\"nebula\" style=\"top: 60%; left: 10%; background: radial-gradient(circle at center, rgba(255, 107, 107, 0.1), transparent 70%);\"></div>\n");
            html.append("    <div class=\"container\">\n");
            html.append("        <h1>🌟 今日の星座占い 🌟</h1>\n");
            html.append("        <div class=\"date\">").append(todayStr).append("</div>\n");
            html.append("        <div class=\"horoscope-grid\">\n");

            for (HoroscopeResult horoscope : horoscopes) {
                html.append("            <a href=\"/detail?zodiac=").append(horoscope.getZodiac().name()).append("\" class=\"horoscope-card\">\n");
                html.append("                <span class=\"zodiac-symbol\">").append(horoscope.getZodiac().getSymbol()).append("</span>\n");
                html.append("                <div>\n");
                html.append("                    <span class=\"ranking ranking-").append(horoscope.getRanking()).append("\">").append(horoscope.getRanking()).append("位</span>\n");
                html.append("                    <span class=\"zodiac-name\">").append(horoscope.getZodiac().getDisplayName()).append("</span>\n");
                html.append("                </div>\n");
                html.append("                <div class=\"fortune\">").append(horoscope.getFortune()).append("</div>\n");
                html.append("                <div class=\"lucky-info\">\n");
                html.append("                    <div class=\"lucky-item\">\n");
                html.append("                        <span class=\"lucky-label\">ラッキーアイテム：</span>\n");
                html.append("                        <span class=\"lucky-value\">").append(horoscope.getLuckyItem()).append("</span>\n");
                html.append("                    </div>\n");
                html.append("                    <div class=\"lucky-item\">\n");
                html.append("                        <span class=\"lucky-label\">ラッキーナンバー：</span>\n");
                html.append("                        <span class=\"lucky-value\">").append(horoscope.getLuckyNumber()).append("</span>\n");
                html.append("                    </div>\n");
                html.append("                    <div class=\"lucky-item\">\n");
                html.append("                        <span class=\"lucky-label\">ラッキーカラー：</span>\n");
                html.append("                        <span class=\"lucky-value\">").append(horoscope.getLuckyColor()).append("</span>\n");
                html.append("                    </div>\n");
                html.append("                </div>\n");
                html.append("            </a>\n");
            }

            html.append("        </div>\n");
            html.append("    </div>\n");
            html.append("    <script>\n");
            html.append("        function createStars() {\n");
            html.append("            const container = document.getElementById('stars-container');\n");
            html.append("            const starCount = 500;\n");
            html.append("            \n");
            html.append("            for (let i = 0; i < starCount; i++) {\n");
            html.append("                const star = document.createElement('div');\n");
            html.append("                star.className = 'star';\n");
            html.append("                \n");
            html.append("                // ランダムな位置\n");
            html.append("                star.style.left = Math.random() * 100 + '%';\n");
            html.append("                star.style.top = Math.random() * 100 + '%';\n");
            html.append("                \n");
            html.append("                // ランダムなサイズ\n");
            html.append("                const size = Math.random() * 3;\n");
            html.append("                star.style.width = size + 'px';\n");
            html.append("                star.style.height = size + 'px';\n");
            html.append("                \n");
            html.append("                // ランダムなアニメーション遅延\n");
            html.append("                star.style.animationDelay = Math.random() * 3 + 's';\n");
            html.append("                \n");
            html.append("                // 一部の星を明るくしたり色を付けたりする\n");
            html.append("                const rand = Math.random();\n");
            html.append("                if (rand > 0.95) {\n");
            html.append("                    star.classList.add('bright');\n");
            html.append("                } else if (rand > 0.9) {\n");
            html.append("                    star.classList.add('yellow');\n");
            html.append("                } else if (rand > 0.85) {\n");
            html.append("                    star.classList.add('blue');\n");
            html.append("                }\n");
            html.append("                \n");
            html.append("                container.appendChild(star);\n");
            html.append("            }\n");
            html.append("        }\n");
            html.append("        \n");
            html.append("        function createShootingStar() {\n");
            html.append("            const container = document.getElementById('stars-container');\n");
            html.append("            const shootingStar = document.createElement('div');\n");
            html.append("            shootingStar.className = 'shooting-star';\n");
            html.append("            \n");
            html.append("            shootingStar.style.left = Math.random() * 50 + '%';\n");
            html.append("            shootingStar.style.top = Math.random() * 50 + '%';\n");
            html.append("            \n");
            html.append("            container.appendChild(shootingStar);\n");
            html.append("            \n");
            html.append("            setTimeout(() => {\n");
            html.append("                shootingStar.remove();\n");
            html.append("            }, 1000);\n");
            html.append("        }\n");
            html.append("        \n");
            html.append("        createStars();\n");
            html.append("        \n");
            html.append("        // 時々流れ星を作る\n");
            html.append("        setInterval(createShootingStar, 5000);\n");
            html.append("    </script>\n");
            html.append("</body>\n");
            html.append("</html>\n");

            return html.toString();
        }
    }
    
    static class DetailHandler implements HttpHandler {
        private final HoroscopeService horoscopeService = new HoroscopeService();
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String zodiacName = null;
            
            if (query != null && query.startsWith("zodiac=")) {
                zodiacName = query.substring(7);
            }
            
            String response;
            if (zodiacName != null) {
                try {
                    Zodiac zodiac = Zodiac.valueOf(zodiacName);
                    response = generateDetailHtml(zodiac);
                } catch (IllegalArgumentException e) {
                    response = generateErrorHtml();
                }
            } else {
                response = generateErrorHtml();
            }
            
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
        
        private String generateDetailHtml(Zodiac zodiac) {
            HoroscopeResult horoscope = horoscopeService.getHoroscopeByZodiac(zodiac);
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
            String todayStr = today.format(formatter);
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"ja\">\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            html.append("    <title>").append(horoscope.getZodiac().getDisplayName()).append(" - 今日の詳細運勢</title>\n");
            html.append("    <style>\n");
            html.append("        body { font-family: 'Hiragino Sans', 'Meiryo', sans-serif; margin: 0; padding: 0; background: #000814; color: #e0e0e0; min-height: 100vh; position: relative; overflow-x: hidden; }\n");
            html.append("        #stars-container { position: fixed; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none; z-index: 0; }\n");
            html.append("        .star { position: absolute; background: white; border-radius: 50%; animation: twinkle 3s ease-in-out infinite; }\n");
            html.append("        .star.bright { box-shadow: 0 0 4px white; }\n");
            html.append("        .star.yellow { background: #ffd60a; box-shadow: 0 0 6px #ffd60a; }\n");
            html.append("        .star.blue { background: #74c0fc; box-shadow: 0 0 4px #74c0fc; }\n");
            html.append("        @keyframes twinkle { 0%, 100% { opacity: 0.3; transform: scale(1); } 50% { opacity: 1; transform: scale(1.2); } }\n");
            html.append("        .milky-way { position: fixed; top: -50%; left: -50%; width: 200%; height: 200%; background: radial-gradient(ellipse at center, transparent 0%, transparent 30%, rgba(138, 43, 226, 0.05) 50%, rgba(30, 0, 50, 0.2) 100%); transform: rotate(-30deg); pointer-events: none; z-index: 0; }\n");
            html.append("        .container { max-width: 800px; margin: 0 auto; padding: 20px; position: relative; z-index: 2; }\n");
            html.append("        .back-link { display: inline-block; margin-bottom: 20px; color: #ffd700; text-decoration: none; font-size: 1.1em; }\n");
            html.append("        .back-link:hover { text-decoration: underline; text-shadow: 0 0 10px rgba(255, 215, 0, 0.5); }\n");
            html.append("        .detail-card { background: rgba(255, 255, 255, 0.1); backdrop-filter: blur(10px); border: 1px solid rgba(255, 255, 255, 0.2); border-radius: 15px; padding: 40px; box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37); position: relative; }\n");
            html.append("        .zodiac-symbol-large { position: absolute; top: 20px; right: 20px; font-size: 5em; opacity: 0.2; color: #ffd700; }\n");
            html.append("        .header { text-align: center; margin-bottom: 40px; }\n");
            html.append("        .zodiac-title { font-size: 2.5em; color: #ffd700; margin-bottom: 10px; text-shadow: 0 0 20px rgba(255, 215, 0, 0.5); }\n");
            html.append("        .date { color: #b8c5d6; font-size: 1.2em; margin-bottom: 20px; }\n");
            html.append("        .ranking-badge { display: inline-block; width: 80px; height: 80px; line-height: 80px; text-align: center; border-radius: 50%; font-weight: bold; font-size: 2em; margin: 20px 0; box-shadow: 0 0 20px rgba(255, 255, 255, 0.3); }\n");
            html.append("        .ranking-1 { background: linear-gradient(135deg, #FFD700, #FFA500); color: #333; box-shadow: 0 0 30px rgba(255, 215, 0, 0.6); }\n");
            html.append("        .ranking-2 { background: linear-gradient(135deg, #C0C0C0, #B8B8B8); color: #333; box-shadow: 0 0 30px rgba(192, 192, 192, 0.6); }\n");
            html.append("        .ranking-3 { background: linear-gradient(135deg, #CD7F32, #B87333); color: white; box-shadow: 0 0 30px rgba(205, 127, 50, 0.6); }\n");
            html.append("        .ranking-4, .ranking-5, .ranking-6 { background: linear-gradient(135deg, #3498db, #2980b9); color: white; }\n");
            html.append("        .ranking-7, .ranking-8, .ranking-9 { background: linear-gradient(135deg, #95a5a6, #7f8c8d); color: white; }\n");
            html.append("        .ranking-10, .ranking-11, .ranking-12 { background: linear-gradient(135deg, #e74c3c, #c0392b); color: white; }\n");
            html.append("        .fortune-section { margin: 30px 0; }\n");
            html.append("        .section-title { font-size: 1.5em; color: #ffd700; margin-bottom: 15px; font-weight: bold; text-shadow: 0 0 10px rgba(255, 215, 0, 0.3); }\n");
            html.append("        .fortune-text { font-size: 1.2em; line-height: 1.8; color: #d0d0d0; margin-bottom: 30px; }\n");
            html.append("        .detail-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin: 30px 0; }\n");
            html.append("        .detail-item { background: rgba(255, 255, 255, 0.1); backdrop-filter: blur(5px); border: 1px solid rgba(255, 255, 255, 0.2); border-radius: 10px; padding: 20px; text-align: center; cursor: pointer; transition: all 0.3s ease; }\n");
            html.append("        .detail-item:hover { background: rgba(255, 255, 255, 0.15); transform: translateY(-2px); }\n");
            html.append("        .detail-label { font-size: 0.9em; color: #a0a0a0; margin-bottom: 10px; }\n");
            html.append("        .detail-value { font-size: 1.3em; font-weight: bold; color: #ffd700; text-shadow: 0 0 5px rgba(255, 215, 0, 0.3); }\n");
            html.append("        .advice-section { background: rgba(41, 128, 185, 0.2); backdrop-filter: blur(5px); border: 1px solid rgba(41, 128, 185, 0.3); border-radius: 10px; padding: 25px; margin: 30px 0; }\n");
            html.append("        .advice-title { font-size: 1.3em; color: #5dade2; margin-bottom: 15px; font-weight: bold; }\n");
            html.append("        .advice-text { line-height: 1.6; color: #d0d0d0; }\n");
            html.append("        .modal { display: none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.8); }\n");
            html.append("        .modal-content { background: rgba(20, 20, 40, 0.95); backdrop-filter: blur(10px); margin: 10% auto; padding: 30px; border: 1px solid rgba(255, 255, 255, 0.2); border-radius: 15px; width: 80%; max-width: 600px; box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37); }\n");
            html.append("        .close { color: #aaa; float: right; font-size: 28px; font-weight: bold; cursor: pointer; }\n");
            html.append("        .close:hover { color: #ffd700; }\n");
            html.append("        .modal-title { color: #ffd700; font-size: 1.5em; margin-bottom: 20px; text-align: center; }\n");
            html.append("        .modal-text { color: #d0d0d0; line-height: 1.8; margin-bottom: 15px; }\n");
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <div id=\"stars-container\"></div>\n");
            html.append("    <div class=\"milky-way\"></div>\n");
            html.append("    <div class=\"container\">\n");
            html.append("        <a href=\"/\" class=\"back-link\">← 一覧に戻る</a>\n");
            html.append("        <div class=\"detail-card\">\n");
            html.append("            <span class=\"zodiac-symbol-large\">").append(horoscope.getZodiac().getSymbol()).append("</span>\n");
            html.append("            <div class=\"header\">\n");
            html.append("                <h1 class=\"zodiac-title\">").append(horoscope.getZodiac().getDisplayName()).append("</h1>\n");
            html.append("                <div class=\"date\">").append(todayStr).append("の運勢</div>\n");
            html.append("                <div class=\"date\" style=\"font-size: 1em; color: #a0a0a0; margin-bottom: 10px;\">").append(getZodiacDateRange(horoscope.getZodiac())).append("</div>\n");
            html.append("                <div class=\"ranking-badge ranking-").append(horoscope.getRanking()).append("\">").append(horoscope.getRanking()).append("位</div>\n");
            html.append("            </div>\n");
            html.append("            \n");
            html.append("            <div class=\"fortune-section\">\n");
            html.append("                <h2 class=\"section-title\">今日の運勢</h2>\n");
            html.append("                <p class=\"fortune-text\">").append(horoscope.getFortune()).append("</p>\n");
            html.append("            </div>\n");
            html.append("            \n");
            html.append("            <div class=\"detail-grid\">\n");
            html.append("                <div class=\"detail-item\">\n");
            html.append("                    <div class=\"detail-label\">ラッキーアイテム</div>\n");
            html.append("                    <div class=\"detail-value\">").append(horoscope.getLuckyItem()).append("</div>\n");
            html.append("                </div>\n");
            html.append("                <div class=\"detail-item\">\n");
            html.append("                    <div class=\"detail-label\">ラッキーナンバー</div>\n");
            html.append("                    <div class=\"detail-value\">").append(horoscope.getLuckyNumber()).append("</div>\n");
            html.append("                </div>\n");
            html.append("                <div class=\"detail-item\">\n");
            html.append("                    <div class=\"detail-label\">ラッキーカラー</div>\n");
            html.append("                    <div class=\"detail-value\">").append(horoscope.getLuckyColor()).append("</div>\n");
            html.append("                </div>\n");
            html.append("                <div class=\"detail-item\">\n");
            html.append("                    <div class=\"detail-label\">ラッキータイム</div>\n");
            html.append("                    <div class=\"detail-value\">").append(generateLuckyTime(horoscope.getRanking())).append("</div>\n");
            html.append("                </div>\n");
            html.append("            </div>\n");
            html.append("            \n");
            
            // ランキングに応じた詳細なアドバイスを追加
            String advice = generateAdvice(horoscope.getRanking());
            html.append("            <div class=\"advice-section\">\n");
            html.append("                <h3 class=\"advice-title\">今日のアドバイス</h3>\n");
            html.append("                <p class=\"advice-text\">").append(advice).append("</p>\n");
            html.append("            </div>\n");
            
            // 各分野の運勢を追加
            html.append("            <div class=\"fortune-section\">\n");
            html.append("                <h2 class=\"section-title\">各分野の運勢（クリックで詳細）</h2>\n");
            html.append("                <div class=\"detail-grid\">\n");
            html.append("                    <div class=\"detail-item\" onclick=\"showModal('love', ").append(horoscope.getRanking()).append(")\">\n");
            html.append("                        <div class=\"detail-label\">恋愛運</div>\n");
            html.append("                        <div class=\"detail-value\">").append(generateStars(horoscope.getRanking(), "love")).append("</div>\n");
            html.append("                    </div>\n");
            html.append("                    <div class=\"detail-item\" onclick=\"showModal('work', ").append(horoscope.getRanking()).append(")\">\n");
            html.append("                        <div class=\"detail-label\">仕事運</div>\n");
            html.append("                        <div class=\"detail-value\">").append(generateStars(horoscope.getRanking(), "work")).append("</div>\n");
            html.append("                    </div>\n");
            html.append("                    <div class=\"detail-item\" onclick=\"showModal('money', ").append(horoscope.getRanking()).append(")\">\n");
            html.append("                        <div class=\"detail-label\">金運</div>\n");
            html.append("                        <div class=\"detail-value\">").append(generateStars(horoscope.getRanking(), "money")).append("</div>\n");
            html.append("                    </div>\n");
            html.append("                    <div class=\"detail-item\" onclick=\"showModal('health', ").append(horoscope.getRanking()).append(")\">\n");
            html.append("                        <div class=\"detail-label\">健康運</div>\n");
            html.append("                        <div class=\"detail-value\">").append(generateStars(horoscope.getRanking(), "health")).append("</div>\n");
            html.append("                    </div>\n");
            html.append("                </div>\n");
            html.append("            </div>\n");
            
            html.append("        </div>\n");
            html.append("    </div>\n");
            html.append("    \n");
            html.append("    <!-- モーダル -->\n");
            html.append("    <div id=\"fortuneModal\" class=\"modal\">\n");
            html.append("        <div class=\"modal-content\">\n");
            html.append("            <span class=\"close\" onclick=\"closeModal()\">&times;</span>\n");
            html.append("            <h3 class=\"modal-title\" id=\"modalTitle\"></h3>\n");
            html.append("            <p class=\"modal-text\" id=\"modalText\"></p>\n");
            html.append("            <p class=\"modal-text\" id=\"modalAdvice\"></p>\n");
            html.append("        </div>\n");
            html.append("    </div>\n");
            html.append("    \n");
            html.append("    <script>\n");
            html.append("        function createStars() {\n");
            html.append("            const container = document.getElementById('stars-container');\n");
            html.append("            const starCount = 300;\n");
            html.append("            \n");
            html.append("            for (let i = 0; i < starCount; i++) {\n");
            html.append("                const star = document.createElement('div');\n");
            html.append("                star.className = 'star';\n");
            html.append("                \n");
            html.append("                star.style.left = Math.random() * 100 + '%';\n");
            html.append("                star.style.top = Math.random() * 100 + '%';\n");
            html.append("                \n");
            html.append("                const size = Math.random() * 3;\n");
            html.append("                star.style.width = size + 'px';\n");
            html.append("                star.style.height = size + 'px';\n");
            html.append("                \n");
            html.append("                star.style.animationDelay = Math.random() * 3 + 's';\n");
            html.append("                \n");
            html.append("                const rand = Math.random();\n");
            html.append("                if (rand > 0.95) {\n");
            html.append("                    star.classList.add('bright');\n");
            html.append("                } else if (rand > 0.9) {\n");
            html.append("                    star.classList.add('yellow');\n");
            html.append("                } else if (rand > 0.85) {\n");
            html.append("                    star.classList.add('blue');\n");
            html.append("                }\n");
            html.append("                \n");
            html.append("                container.appendChild(star);\n");
            html.append("            }\n");
            html.append("        }\n");
            html.append("        \n");
            html.append("        createStars();\n");
            html.append("        \n");
            html.append("        // モーダル機能\n");
            html.append("        function showModal(category, ranking) {\n");
            html.append("            const modal = document.getElementById('fortuneModal');\n");
            html.append("            const title = document.getElementById('modalTitle');\n");
            html.append("            const text = document.getElementById('modalText');\n");
            html.append("            const advice = document.getElementById('modalAdvice');\n");
            html.append("            \n");
            html.append("            const titles = {\n");
            html.append("                love: '恋愛運の詳細',\n");
            html.append("                work: '仕事運の詳細',\n");
            html.append("                money: '金運の詳細',\n");
            html.append("                health: '健康運の詳細'\n");
            html.append("            };\n");
            html.append("            \n");
            html.append("            const fortunes = {\n");
            html.append("                love: {\n");
            html.append("                    good: '恋愛運は絶好調！素敵な出会いや、パートナーとの関係が深まる暗示があります。積極的にコミュニケーションを取ることで、より良い関係を築けるでしょう。',\n");
            html.append("                    normal: '恋愛運は安定しています。焦らず自然体でいることが大切です。相手の気持ちを考えながら行動すると良いでしょう。',\n");
            html.append("                    bad: '恋愛運は少し低調です。今は自分磨きに専念する時期かもしれません。焦らず、タイミングを待ちましょう。'\n");
            html.append("                },\n");
            html.append("                work: {\n");
            html.append("                    good: '仕事運は最高潮！新しいプロジェクトや昇進のチャンスが訪れそうです。積極的に挑戦することで、大きな成果を得られるでしょう。',\n");
            html.append("                    normal: '仕事運は順調です。コツコツと努力を続けることで、着実に成果を上げられます。チームワークを大切にしましょう。',\n");
            html.append("                    bad: '仕事運は少し注意が必要です。ミスを防ぐため、いつも以上に慎重に行動しましょう。休息も大切です。'\n");
            html.append("                },\n");
            html.append("                money: {\n");
            html.append("                    good: '金運は絶好調！臨時収入や投資のチャンスがありそうです。ただし、使いすぎには注意しましょう。',\n");
            html.append("                    normal: '金運は安定しています。計画的な支出を心がけることで、着実に貯蓄を増やせるでしょう。',\n");
            html.append("                    bad: '金運は少し低調です。衝動買いは避け、必要なものだけに支出を絞りましょう。'\n");
            html.append("                },\n");
            html.append("                health: {\n");
            html.append("                    good: '健康運は絶好調！エネルギーに満ち溢れています。新しい運動や健康法を始めるのに最適な時期です。',\n");
            html.append("                    normal: '健康運は安定しています。規則正しい生活を心がけることで、良い状態を維持できるでしょう。',\n");
            html.append("                    bad: '健康運は少し注意が必要です。無理をせず、十分な休息を取ることを心がけましょう。'\n");
            html.append("                }\n");
            html.append("            };\n");
            html.append("            \n");
            html.append("            const advices = {\n");
            html.append("                love: {\n");
            html.append("                    good: 'アドバイス：自信を持って行動しましょう。あなたの魅力が最大限に発揮される時期です。',\n");
            html.append("                    normal: 'アドバイス：相手の立場に立って考えることで、より良い関係を築けます。',\n");
            html.append("                    bad: 'アドバイス：今は充電期間と考え、自分自身と向き合う時間を大切にしましょう。'\n");
            html.append("                },\n");
            html.append("                work: {\n");
            html.append("                    good: 'アドバイス：チャンスを逃さないよう、アンテナを高く保ちましょう。',\n");
            html.append("                    normal: 'アドバイス：基本に忠実に、丁寧な仕事を心がけることが成功への近道です。',\n");
            html.append("                    bad: 'アドバイス：焦らず、一つ一つ確実にこなしていくことが大切です。'\n");
            html.append("                },\n");
            html.append("                money: {\n");
            html.append("                    good: 'アドバイス：良い運気ですが、計画的な資産運用を心がけましょう。',\n");
            html.append("                    normal: 'アドバイス：小さな節約の積み重ねが、大きな成果につながります。',\n");
            html.append("                    bad: 'アドバイス：今は守りの時期。新しい投資は控えめにしましょう。'\n");
            html.append("                },\n");
            html.append("                health: {\n");
            html.append("                    good: 'アドバイス：この好調を維持するため、生活習慣を整えましょう。',\n");
            html.append("                    normal: 'アドバイス：バランスの良い食事と適度な運動を心がけましょう。',\n");
            html.append("                    bad: 'アドバイス：体の声に耳を傾け、無理をしないことが大切です。'\n");
            html.append("                }\n");
            html.append("            };\n");
            html.append("            \n");
            html.append("            const level = ranking <= 4 ? 'good' : ranking <= 8 ? 'normal' : 'bad';\n");
            html.append("            \n");
            html.append("            title.textContent = titles[category];\n");
            html.append("            text.textContent = fortunes[category][level];\n");
            html.append("            advice.textContent = advices[category][level];\n");
            html.append("            \n");
            html.append("            modal.style.display = 'block';\n");
            html.append("        }\n");
            html.append("        \n");
            html.append("        function closeModal() {\n");
            html.append("            document.getElementById('fortuneModal').style.display = 'none';\n");
            html.append("        }\n");
            html.append("        \n");
            html.append("        // モーダル外をクリックしたら閉じる\n");
            html.append("        window.onclick = function(event) {\n");
            html.append("            const modal = document.getElementById('fortuneModal');\n");
            html.append("            if (event.target == modal) {\n");
            html.append("                modal.style.display = 'none';\n");
            html.append("            }\n");
            html.append("        }\n");
            html.append("    </script>\n");
            html.append("</body>\n");
            html.append("</html>\n");
            
            return html.toString();
        }
        
        private String generateAdvice(int ranking) {
            if (ranking <= 3) {
                return "今日は絶好調の一日です！積極的に行動することで、素晴らしい結果が得られるでしょう。新しいことにチャレンジするのに最適な日です。周りの人にも幸運を分けてあげましょう。";
            } else if (ranking <= 6) {
                return "安定した良い運気の日です。普段通りの行動を心がけながら、少し冒険してみるのも良いでしょう。人間関係を大切にすることで、さらに運気が上昇します。";
            } else if (ranking <= 9) {
                return "今日は慎重に行動することが大切です。大きな決断は避け、じっくりと考える時間を持ちましょう。小さな幸せを見つけることで、気持ちが前向きになります。";
            } else {
                return "今日は充電の日と考えましょう。無理をせず、リラックスして過ごすことが大切です。明日への準備期間と捉え、心身を整えることに集中しましょう。";
            }
        }
        
        private String generateStars(int ranking, String category) {
            int stars;
            switch (category) {
                case "love":
                    stars = ranking <= 4 ? 5 : ranking <= 8 ? 3 : 2;
                    break;
                case "work":
                    stars = ranking <= 3 ? 5 : ranking <= 7 ? 4 : 2;
                    break;
                case "money":
                    stars = ranking <= 5 ? 4 : ranking <= 9 ? 3 : 2;
                    break;
                case "health":
                    stars = ranking <= 6 ? 4 : ranking <= 10 ? 3 : 2;
                    break;
                default:
                    stars = 3;
            }
            
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                if (i < stars) {
                    result.append("★");
                } else {
                    result.append("☆");
                }
            }
            return result.toString();
        }
        
        private String getZodiacDateRange(Zodiac zodiac) {
            String startMonth = zodiac.getStartMonth() + "月";
            String startDay = zodiac.getStartDay() + "日";
            String endMonth = zodiac.getEndMonth() + "月";
            String endDay = zodiac.getEndDay() + "日";
            
            // 年をまたぐ星座（山羊座）の場合の特別な処理
            if (zodiac == Zodiac.CAPRICORN) {
                return "12月22日 ～ 1月19日生まれ";
            }
            
            return startMonth + startDay + " ～ " + endMonth + endDay + "生まれ";
        }
        
        private String generateLuckyTime(int ranking) {
            String[] times = {
                "7:00～9:00", "9:00～11:00", "11:00～13:00",
                "13:00～15:00", "15:00～17:00", "17:00～19:00",
                "19:00～21:00", "21:00～23:00", "10:00～12:00",
                "14:00～16:00", "16:00～18:00", "20:00～22:00"
            };
            
            // ランキングに基づいて時間を選択（1位は朝、12位は夜など）
            return times[ranking - 1];
        }
        
        private String generateErrorHtml() {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"ja\">\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <title>エラー</title>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <h1>エラーが発生しました</h1>\n");
            html.append("    <p><a href=\"/\">トップページに戻る</a></p>\n");
            html.append("</body>\n");
            html.append("</html>\n");
            return html.toString();
        }
    }
}