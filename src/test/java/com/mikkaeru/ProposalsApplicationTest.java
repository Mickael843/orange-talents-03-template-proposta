package com.mikkaeru;

import com.mikkaeru.helper.IntegrationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProposalsApplicationTest extends IntegrationHelper {

    @Test
    void contextLoads() {
    }
}