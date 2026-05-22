package com.econo_4factorial.newproject.base.service.weather;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

class WeatherSchedulerContextTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withPropertyValues("weather.scheduler.cron=0 0 0 * * *");

    @Test
    void startup_jobs가_false면_WeatherScheduler_빈이_생성되지_않는다() {
        contextRunner
                .withPropertyValues("app.startup-jobs.enabled=false")
                .run(context -> assertThat(context).doesNotHaveBean(WeatherScheduler.class));
    }

    @Test
    void startup_jobs가_true면_WeatherScheduler_빈이_생성된다() {
        contextRunner
                .withPropertyValues("app.startup-jobs.enabled=true")
                .run(context -> assertThat(context).hasSingleBean(WeatherScheduler.class));
    }

    @Configuration(proxyBeanMethods = false)
    @ComponentScan(
            basePackageClasses = WeatherScheduler.class,
            useDefaultFilters = false,
            includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WeatherScheduler.class)
    )
    static class TestConfig {
        @Bean
        WeatherService weatherService() {
            return mock(WeatherService.class);
        }
    }
}
