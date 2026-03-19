package com.econo_4factorial.newproject;

import com.econo_4factorial.newproject.support.MySqlRedisContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class NewProjectApplicationTests extends MySqlRedisContainerSupport {

    @Test
    void 스프링_컨텍스트가_정상적으로_로드된다() {
    }

}
