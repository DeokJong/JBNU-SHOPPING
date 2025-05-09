package com.cottongallery.backend.common.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisInitializer {

  private final RedisConnectionFactory redisConnectionFactory;

  @Value("${SPRING_PROFILES_ACTIVE}")
  private String activeProfile;

  @PostConstruct
  public void enableKeyspaceNotifications() {
    if ("aws".equals(activeProfile)) {
      log.info("Skipping keyspace notifications setup for profile: {}", activeProfile);
    } else {
      try (RedisConnection connection = redisConnectionFactory.getConnection()) {
        // Redis CONFIG SET notify-keyspace-events Ex
        byte[] set = "SET".getBytes();
        byte[] key = "notify-keyspace-events".getBytes();
        byte[] value = "Ex".getBytes();

        connection.execute("CONFIG", set, key, value);
        log.info("Keyspace notifications enabled for local profile.");
      } catch (Exception e) {
        log.error("Failed to enable keyspace notifications for local profile.", e);
      }

    }
  }
}
