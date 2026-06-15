package com.econo_4factorial.newproject.facility;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import com.econo_4factorial.newproject.auth.jwt.service.AuthTokenService;
import com.econo_4factorial.newproject.support.E2eTestSupport;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

class FacilityE2eTest extends E2eTestSupport {

    private static final Long USER_ID = 1L;
    private static final Long MUDEUNGSAN_ID = 1L;
    private static final Long BUKHANSAN_ID = 2L;
    private static final Long NOT_EXIST_MOUNTAIN_ID = 99L;

    @Autowired
    private AuthTokenService authTokenService;

    @Test
    void 산의_시설_목록을_조회한다() {
        authenticated()
                .accept(ContentType.JSON)
                .queryParam("mountainId", MUDEUNGSAN_ID)
                .when()
                .get("/api/v1/facilities")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.mountainId", equalTo(MUDEUNGSAN_ID.intValue()))
                .body("data.facilities.facilityName", hasItem("아이더"))
                .body("data.facilities.find { it.facilityName == '아이더' }.facilityType", equalTo("MARKET"));
    }

    @Test
    void 시설이_없는_산은_빈_목록을_반환한다() {
        authenticated()
                .accept(ContentType.JSON)
                .queryParam("mountainId", BUKHANSAN_ID)
                .when()
                .get("/api/v1/facilities")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.facilities.size()", equalTo(0));
    }

    @Test
    void 존재하지_않는_산의_시설을_조회하면_404를_반환한다() {
        authenticated()
                .accept(ContentType.JSON)
                .queryParam("mountainId", NOT_EXIST_MOUNTAIN_ID)
                .when()
                .get("/api/v1/facilities")
                .then()
                .statusCode(404)
                .body("status", equalTo("error"))
                .body("errorCode", equalTo("MOUNTAIN404_001"));
    }

    @Test
    void 산_아이디가_없으면_400을_반환한다() {
        authenticated()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/facilities")
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
