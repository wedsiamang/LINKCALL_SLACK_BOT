package com.example.linkcall;

// なぜこのコードか：
// @Entityでテーブル自動生成、@Idで主キー、
// @GeneratedValueで自動採番
// Lombokなしでもフィールドアクセスはgetterで十分

import jakarta.persistence.*;

@Entity
public class KeywordLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String keyword;

    private String url;

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}

