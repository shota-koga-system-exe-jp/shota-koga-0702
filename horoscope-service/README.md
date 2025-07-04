# 星座占いWebサービス

Spring Bootを使用した簡単な星座占いWebサービスです。

## 機能

- その日の星座占いを1位から12位まで表示
- 各星座の運勢の解説を表示
- ラッキーアイテム、ラッキーナンバー、ラッキーカラーを表示
- 日付に基づいて毎日異なる占い結果を生成

## 技術スタック

- Java 17
- Spring Boot 3.2.0
- Spring MVC
- Thymeleaf（テンプレートエンジン）
- Maven（ビルドツール）

## プロジェクト構造

```
horoscope-service/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/horoscope/
│       │       ├── HoroscopeServiceApplication.java
│       │       ├── controller/
│       │       │   └── HoroscopeController.java
│       │       ├── model/
│       │       │   ├── Zodiac.java
│       │       │   └── HoroscopeResult.java
│       │       └── service/
│       │           └── HoroscopeService.java
│       └── resources/
│           ├── application.properties
│           └── templates/
│               └── index.html
└── README.md
```

## 実行方法

1. プロジェクトディレクトリに移動
```bash
cd horoscope-service
```

2. Mavenでビルド
```bash
mvn clean install
```

3. アプリケーションを起動
```bash
mvn spring-boot:run
```

4. ブラウザでアクセス
```
http://localhost:8080
```

## 占いのロジック

- 日付をシードとして使用し、毎日同じ日付では同じ結果が表示されます
- 12星座をランダムにシャッフルして順位を決定
- ランキングに応じて運勢のメッセージを割り当て
- ラッキーアイテム、ナンバー、カラーはランダムに選択

## カスタマイズ

`HoroscopeService.java`の以下の配列を編集することで、占いの内容をカスタマイズできます：
- `FORTUNES`: 運勢のメッセージ
- `LUCKY_ITEMS`: ラッキーアイテムのリスト
- `LUCKY_COLORS`: ラッキーカラーのリスト