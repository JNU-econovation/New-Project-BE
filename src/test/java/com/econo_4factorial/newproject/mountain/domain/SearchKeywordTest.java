package com.econo_4factorial.newproject.mountain.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.Normalizer;
import org.junit.jupiter.api.Test;

class SearchKeywordTest {

    @Test
    void null_키워드는_빈_키워드다() {
        SearchKeyword keyword = new SearchKeyword(null);

        assertThat(keyword.isEmpty()).isTrue();
    }

    @Test
    void 공백뿐인_키워드는_빈_키워드다() {
        SearchKeyword keyword = new SearchKeyword("   ");

        assertThat(keyword.isEmpty()).isTrue();
    }

    @Test
    void 앞뒤_공백을_제거한다() {
        SearchKeyword keyword = new SearchKeyword("  무등  ");

        assertThat(keyword.value()).isEqualTo("무등");
    }

    @Test
    void 자소분리된_입력을_완성형으로_정규화한다() {
        String decomposed = Normalizer.normalize("무등", Normalizer.Form.NFD);

        SearchKeyword keyword = new SearchKeyword(decomposed);

        assertThat(keyword.value()).isEqualTo("무등");
    }

    @Test
    void 초성으로만_이루어진_키워드는_초성_키워드다() {
        SearchKeyword keyword = new SearchKeyword("ㅁㄷ");

        assertThat(keyword.isInitials()).isTrue();
    }

    @Test
    void 쌍자음_초성도_초성_키워드다() {
        SearchKeyword keyword = new SearchKeyword("ㄲㄸㅃㅆㅉ");

        assertThat(keyword.isInitials()).isTrue();
    }

    @Test
    void 일반_단어는_초성_키워드가_아니다() {
        SearchKeyword keyword = new SearchKeyword("무등");

        assertThat(keyword.isInitials()).isFalse();
    }

    @Test
    void 초성과_일반_글자가_섞이면_초성_키워드가_아니다() {
        SearchKeyword keyword = new SearchKeyword("ㅁ등");

        assertThat(keyword.isInitials()).isFalse();
    }

    @Test
    void 빈_키워드는_초성_키워드가_아니다() {
        SearchKeyword keyword = new SearchKeyword("");

        assertThat(keyword.isInitials()).isFalse();
    }
}
