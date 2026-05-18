// なぜこのコードか：
// BoltはSlack公式JavaフレームワークでApp/AppConfigがエントリポイント
// @Valueでapplication.yamlのslack.*を注入

package com.example.linkcall;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    @Value("${slack.bot-token}")
    private String botToken;

    @Value("${slack.app-token}")
    private String appToken;

    @Bean
    public AppConfig appConfig() {
        return AppConfig.builder()
                .singleTeamBotToken(botToken)
                .build();
    }

    @Bean
    public App app(AppConfig appConfig) {
        return new App(appConfig);
    }

    public String getAppToken() { return appToken; }
}