# LINKCALL_SLACK_BOT

> Claude（AI）を活用して開発しました。
> Java Silver取得後、Spring Boot学習中のため、
> AIが生成したコードを読み解くスタイルで実装しています。

Slackのメンションで、キーワードに紐づくURLを返すBot。

## 背景

Slackを使っている人の「あのリンクどこ」を解決するために作成。
ブックマークを探さずに、Slackから直接リンクを呼び出せます。

## 技術スタック

- Java 17
- Spring Boot 3.5.14
- Slack Bolt SDK (Socket Mode)
- Spring Data JPA / H2 Database (ファイルモード)

> `https://start.spring.io/` でプロジェクト生成後、Slack APIを手動追加。
> H2ファイルモードのため、DB設定不要でデータ永続化が可能。

## 機能

| コマンド | 動作 |
|---|---|
| `@bot 登録 キーワード URL` | URLを登録 |
| `@bot キーワード` | URLを返却 |
| `@bot 削除 キーワード` | 削除 |
| `@bot 一覧` | 登録済み一覧表示 |

## セットアップ

`.env.example`にトークンを設定後:

```
mvn spring-boot:run
```
