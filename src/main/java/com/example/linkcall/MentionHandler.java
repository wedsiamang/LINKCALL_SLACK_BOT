// なぜこのコードか：
// app_mentionイベントのevent.textからBot IDを除去してコマンド判定
// SocketModeClientをCommandLineRunnerで起動することでSpring管理下に置く
// 参考: https://slack.dev/java-slack-sdk/guides/socket-mode

package com.example.linkcall;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.methods.MethodsClient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MentionHandler implements CommandLineRunner {

    private final App app;
    private final SlackConfig slackConfig;
    private final KeywordLinkRepository repository;

    public MentionHandler(App app, SlackConfig slackConfig, KeywordLinkRepository repository) {
        this.app = app;
        this.slackConfig = slackConfig;
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {

        app.event(com.slack.api.model.event.AppMentionEvent.class, (payload, ctx) -> {
            String text = payload.getEvent().getText()
                    .replaceAll("<@[A-Z0-9]+>", "").trim();
            handle(text, payload.getEvent().getChannel(),ctx.client());
            return ctx.ack();
        });

        // DM対応
        app.event(com.slack.api.model.event.MessageEvent.class, (payload, ctx) -> {
            if ("im".equals(payload.getEvent().getChannelType())) {
                String text = payload.getEvent().getText().trim();
                handle(text, payload.getEvent().getChannel(),ctx.client());
            }
            return ctx.ack();
        });

        new SocketModeApp(slackConfig.getAppToken(), app).start();
    }
private void handle(String text, String channel, MethodsClient client) {
   try{
        String[] parts = text.split("\\s+", 3);

        if (parts[0].equals("登録") && parts.length == 3) {
            KeywordLink kl = new KeywordLink();
            kl.setKeyword(parts[1]);
            kl.setUrl(parts[2]);
            repository.save(kl);
            client.chatPostMessage(r -> r.channel(channel).text("登録しました: " + parts[1]));

        } else if (parts[0].equals("削除") && parts.length == 2) {
            repository.findByKeyword(parts[1]).ifPresent(repository::delete);
            client.chatPostMessage(r -> r.channel(channel).text("削除しました: " + parts[1]));

        } else if (parts[0].equals("一覧")) {
            StringBuilder sb = new StringBuilder("登録済みキーワード:\n");
            repository.findAll().forEach(kl -> sb.append("• ").append(kl.getKeyword()).append("\n"));
            client.chatPostMessage(r -> r.channel(channel).text(sb.toString()));

        } else {
            repository.findByKeyword(parts[0]).ifPresentOrElse(
                kl -> {
                    try {
                        client.chatPostMessage(r -> r.channel(channel).text(kl.getUrl()));
                    } catch (Exception e) { e.printStackTrace(); }
                },
                () -> {
                    try {
                        client.chatPostMessage(r -> r.channel(channel).text("キーワードが見つかりません: " + parts[0]));
                    } catch (Exception e) { e.printStackTrace(); }
                }
            );
        }
   }catch(Exception e){
    e.printStackTrace();
   }
    }
}