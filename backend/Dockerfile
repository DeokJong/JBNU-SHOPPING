#=================================================================
# BUILD STAGE 1 - JRE 추출
#=================================================================
FROM amazoncorretto:17-alpine3.18 AS builder-jre

# Step 2: binutills 설치
RUN apk add --no-cache binutils

# Step 3: jlink 사용하여 최소화된 JRE 생성
RUN $JAVA_HOME/bin/jlink \
         --module-path "$JAVA_HOME/jmods" \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /jre

#=================================================================
# BUILD STAGE 2 - Gradle을 사용하여 애플리케이션 빌드
#=================================================================
FROM gradle:7.6-jdk17-alpine AS builder-app

# 애플리케이션 파일 및 Gradle 래퍼 복사
WORKDIR /workspace
COPY . .

# 애플리케이션 빌드
RUN gradle build -x test

#=================================================================
# BUILD STAGE 3 - APP 실행
#=================================================================
FROM alpine:3.18.4

# 환경 설정
ENV JAVA_HOME=/jre
ENV PATH="$JAVA_HOME/bin:$PATH"

# 이전 빌드 단계에서 최소화된 JRE 복사
COPY --from=builder-jre /jre $JAVA_HOME

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 빌드 단계에서 빌드된 JAR 복사
COPY --from=builder-app /workspace/build/libs/*.jar app.jar

RUN mkdir -p /app/resources && chmod -R 755 /app/resources

# 애플리케이션 포트 노출
EXPOSE 8080

ENV AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar"]
