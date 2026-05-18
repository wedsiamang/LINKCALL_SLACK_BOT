## LINKCALL_SLACK_BOT

> Claude（AI）を活用して開発しました。
> Java Silver取得後、Spring Boot学習中のため、
> AIが生成したコードでappを動かし、コードを読み解くを繰り返して学習速度を上げています。

Slackのメンションで、キーワードに紐づくURLを返すBot。

### 背景

Slackを使っている人の「あのリンクどこ」を解決するために作成。
ブックマークを探さずに、Slackから直接リンクを呼び出せます。

### 技術スタック

- Java 17
- Spring Boot 3.5.14
- Slack Bolt SDK (Socket Mode)
- Spring Data JPA / H2 Database (ファイルモード)

> `https://start.spring.io/` でプロジェクト生成後、Slack APIを手動追加。
> H2ファイルモードのため、DB設定不要でデータ永続化が可能。

### 機能

| コマンド | 動作 |
|---|---|
| `@bot 登録 キーワード URL` | URLを登録 |
| `@bot キーワード` | URLを返却 |
| `@bot 削除 キーワード` | 削除 |
| `@bot 一覧` | 登録済み一覧表示 |

### セットアップ

`.env.example`にトークンを設定後:
```
mvn spring-boot:run
```
---

### Slack 設定

##### Bot-token-Scope
- app_mentions:read  アプリが参加している会話で @linkCall を直接メンションしているメッセージを表示
- chat:write  アプリが参加している会話で @linkCall を直接メンションしているメッセージを表示
- im:history  "linkCall"が連携されたダイレクトメッセージでメッセージやその他のコンテンツにアクセスする

##### Event subscription
Enable Events ,Delayed Events をオンにする
- messageim  bot との DM ができる
- app_mention app または bot をメンション
　*AppHome > Messages Tab をオン、Allows users to send Slash commands and messages from the message tab にチェック

---

## コード理解　📖

### KeywordLink.java
Entityクラスです。
キーワードとURLを保持する変数のみのシンプルな構成のため、Lombokは使わずgetter/setterを手書きしています。

### KeywordLinkRepository.java
Repositoryクラスです。
JpaRepositoryを継承したインターフェースで、継承するだけでCRUD操作が使えるようになります。

### LinkcallApplication.java
Spring Initializrが生成するデフォルトクラスです。
mainメソッドがあり、ここからアプリが起動します。

### SlackConfig.java
Slackのトークン情報をapplication.ymlから読み込み、AppConfigとAppのBeanを生成しています。

### MentionHandler.java
イベント処理クラスです。
SlackConfigとRepositoryを受け取り、
app_mentionとDMの2つのイベントを処理します。

メンションを受信すると、まずBot IDを正規表現で除去し、
テキスト(parts)を最大3つに分割します。
先頭の単語で処理を分岐します。

- `登録` → KeywordLinkをインスタンス化してキーワードとURLをセットし、
  repository.save()でH2に保存します。
- `削除` → キーワードで検索し、見つかれば削除します。
- `一覧` → 登録済みの全キーワードを返します。
- それ以外 → キーワードとして検索し、URLを返します。
- bot とのDMでは、メンションなしでキーワードで結果が返ります。
