# GitHub Actions トラブルシューティング

## GitHub Actionsが動かない場合の確認事項

### 1. リポジトリでActionsが有効になっているか確認

1. GitHubリポジトリにアクセス
2. Settings タブをクリック
3. 左側メニューの「Actions」→「General」を選択
4. 「Actions permissions」が以下のいずれかになっているか確認：
   - "Allow all actions and reusable workflows"
   - "Allow shota-koga-system-exe-jp actions and reusable workflows"

### 2. 手動でワークフローを実行

1. GitHubリポジトリの「Actions」タブをクリック
2. 左側のワークフロー一覧から「Deploy to GitHub Pages」を選択
3. 右側の「Run workflow」ボタンをクリック
4. ブランチを選択（main）して「Run workflow」をクリック

### 3. ワークフローファイルの確認

以下のコマンドで変更をコミット・プッシュ：

```bash
git add .github/workflows/deploy.yml
git commit -m "fix: GitHub Actions ワークフローファイルを修正"
git push origin main
```

### 4. GitHub Pages の設定確認

1. Settings → Pages
2. 「Build and deployment」セクションで：
   - Source: 「GitHub Actions」を選択
   - もし「Deploy from a branch」になっている場合は「GitHub Actions」に変更

### 5. リポジトリの可視性

- プライベートリポジトリの場合、GitHub Pages機能が制限される場合があります
- 無料プランの場合は、パブリックリポジトリでのみGitHub Pagesが利用可能です

## それでも動かない場合

1. Actions タブで過去の実行履歴を確認
2. エラーメッセージがある場合は、その内容を確認
3. `.github/workflows/`ディレクトリが正しくコミットされているか確認