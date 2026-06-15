package com.econo_4factorial.newproject;

import com.econo_4factorial.newproject.support.MySqlRedisContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NewProjectApplicationTests extends MySqlRedisContainerSupport {

    @Test
    void contextLoads() {
    }
}
