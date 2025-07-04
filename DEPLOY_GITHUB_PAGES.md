# GitHub Pages デプロイ設定

このプロジェクトはGitHub Actionsを使用してGitHub Pagesに自動デプロイされるように設定されています。

## セットアップ手順

1. **GitHub リポジトリの設定**
   - GitHubリポジトリの Settings > Pages に移動
   - Source を「GitHub Actions」に設定

2. **自動デプロイ**
   - `main`ブランチにプッシュすると自動的にデプロイが開始されます
   - GitHub Actions タブでデプロイの進行状況を確認できます

3. **デプロイ先URL**
   - https://shota-koga-system-exe-jp.github.io/shota-koga-0702/

## ローカルでのテスト

```bash
cd horoscope-nextjs
npm install
npm run build
```

ビルドが成功すると、`horoscope-nextjs/out`ディレクトリに静的ファイルが生成されます。

## トラブルシューティング

- デプロイが失敗する場合は、GitHub Actions のログを確認してください
- 404エラーが発生する場合は、`next.config.ts`の`basePath`設定を確認してください
- 画像が表示されない場合は、相対パスが正しく設定されているか確認してください