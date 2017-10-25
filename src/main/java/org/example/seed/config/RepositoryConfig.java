package org.example.seed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Created by Ricardo Pina Arellano on 24/11/2016.
 */

@Configuration
@EnableJpaAuditing
@ComponentScan(basePackages = {"org.example.seed.repository"})
public class RepositoryConfig {

  @Bean
  public AuditingEntityListener createAuditingListener() {
    return new AuditingEntityListener();
  }
}
