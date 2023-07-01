FROM arm64v8/gradle as dependencies-builder
ENV APP_DIR /app
WORKDIR $APP_DIR

RUN mkdir /tmp/cache

COPY build.gradle.kts $APP_DIR/
COPY settings.gradle.kts $APP_DIR/
COPY gradle.properties $APP_DIR/
RUN gradle dependencies -g /tmp/cache

# -----------------------------------------------------------------------------
FROM arm64v8/gradle as builder
ENV APP_DIR /app
WORKDIR $APP_DIR
RUN mkdir /tmp/cache
COPY --from=dependencies-builder /tmp/cache /tmp/cache
COPY . $APP_DIR
RUN gradle assemble -g /tmp/cache --no-daemon

# -----------------------------------------------------------------------------
FROM arm64v8/eclipse-temurin

WORKDIR /deployments

COPY --from=builder /app/build/libs/*.jar /deployments/

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar leaderboard-3.0.0.jar"]