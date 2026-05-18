// なぜこのコードか：
// JpaRepositoryを継承するだけでCRUD自動生成
// findByKeywordはメソッド名からSQLを自動生成するSpring Data JPAの機能

package com.example.linkcall;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KeywordLinkRepository extends JpaRepository<KeywordLink, Long> {
    Optional<KeywordLink> findByKeyword(String keyword);
}
