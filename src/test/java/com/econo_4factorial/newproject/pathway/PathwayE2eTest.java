package com.econo_4factorial.newproject.pathway;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.econo_4factorial.newproject.auth.jwt.service.AuthTokenService;
import com.econo_4factorial.newproject.support.E2eTestSupport;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

class PathwayE2eTest extends E2eTestSupport {

    private static final Long USER_ID = 1L;
    private static final Long COURSE_ID = 1L;
    private static final Long COURSE_WITHOUT_PATHWAY_ID = 2L;

    @Autowired
    private AuthTokenService authTokenService;

    @Test
    void 코스의_경로를_순서대로_조회한다() {
        authenticated()
                .accept(ContentType.JSON)
                .queryParam("courseId", COURSE_ID)
                .when()
                .get("/api/v1/pathways")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.pathways.size()", equalTo(3))
                // sequence 1→2→3 순서대로 dept/dest가 나오는지 (순서 + 매퍼 dept/dest 고정)
                .body("data.pathways[0].deptBaseId", equalTo(21))
                .body("data.pathways[0].destBaseId", equalTo(11))
                .body("data.pathways[1].deptBaseId", equalTo(11))
                .body("data.pathways[1].destBaseId", equalTo(13))
                .body("data.pathways[2].deptBaseId", equalTo(13))
                .body("data.pathways[2].destBaseId", equalTo(28))
                .body("data.pathways[2].difficulty", equalTo("HARD"))
                // 좌표 축 순서: 첫 점이 시드값과 정확히 일치하는지 (= [경도, 위도]).
                // 시드 WKT 'LINESTRING(35.1334 126.9578 ...)' = 위도 먼저지만,
                // SRID 4326 축 swap으로 getX=경도가 되어 API는 [126.9578(경도), 35.1334(위도)]로 나온다 (mountain과 일치).
                .body("data.pathways[0].coordinates[0][0]", equalTo(126.9578f))
                .body("data.pathways[0].coordinates[0][1]", equalTo(35.1334f));
    }

    @Test
    void 경로가_없는_코스는_빈_목록을_반환한다() {
        authenticated()
                .accept(ContentType.JSON)
                .queryParam("courseId", COURSE_WITHOUT_PATHWAY_ID)
                .when()
                .get("/api/v1/pathways")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.pathways.size()", equalTo(0));
    }

    @Test
    void 코스_아이디가_없으면_400을_반환한다() {
        authenticated()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/pathways")
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
