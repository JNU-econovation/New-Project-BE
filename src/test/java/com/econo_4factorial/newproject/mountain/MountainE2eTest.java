package com.econo_4factorial.newproject.mountain;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

import com.econo_4factorial.newproject.auth.jwt.service.AuthTokenService;
import com.econo_4factorial.newproject.support.E2eTestSupport;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

class MountainE2eTest extends E2eTestSupport {

    private static final Long USER_ID = 1L;

    @Autowired
    private AuthTokenService authTokenService;

    @Test
    void 전체_산_목록을_조회한다() {
        authenticated()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/mountains")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.mountains.size()", equalTo(11))
                .body("data.mountains.name", hasItem("무등산"));
    }

    @Test
    void 초성으로_산을_자동완성한다() {
        authenticated()
                .accept(ContentType.JSON)
                .queryParam("keyword", "ㅁㄷ")
                .when()
                .get("/api/v1/mountains/searches/suggestions")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.suggestedMountainDTOs.size()", equalTo(1))
                .body("data.suggestedMountainDTOs[0].name", equalTo("무등산"));
    }

    @Test
    void 단어로_산을_자동완성한다() {
        authenticated()
                .accept(ContentType.JSON)
                .queryParam("keyword", "백")
                .when()
                .get("/api/v1/mountains/searches/suggestions")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.suggestedMountainDTOs.name", hasItems("태백산", "소백산"));
    }

    @Test
    void 빈_키워드는_빈_목록을_반환한다() {
        authenticated()
                .accept(ContentType.JSON)
                .queryParam("keyword", "")
                .when()
                .get("/api/v1/mountains/searches/suggestions")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.suggestedMountainDTOs.size()", equalTo(0));
    }

    @Test
    void 자동완성_키워드가_없으면_400을_반환한다() {
        authenticated()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/mountains/searches/suggestions")
                .then()
                .statusCode(400)
                .body("status", equalTo("error"))
                .body("errorCode", equalTo("COMMON400_004"));
    }

    private RequestSpecification authenticated() {
        String accessToken = authTokenService.issueAuthToken(USER_ID).accessToken();
        return given().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }
}
