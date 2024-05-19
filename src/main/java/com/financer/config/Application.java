/* 
 * Adriel Swisher
 * CST 452
 * 
 * Application class to run Spring application and establish layout dialect for Thymeleaf layouts
*/
package com.financer.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@EnableJpaRepositories("com.financer.persistence.*")
@EntityScan("com.financer.persistence.*")
@SpringBootApplication(scanBasePackages = {"com.financer.*"})
public class Application{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}
