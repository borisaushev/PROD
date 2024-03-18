FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && cd target/dependency && jar -xf ../*.jar

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

ARG DEPENDENCY=/workspace/app/target/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENV SERVER_PORT=8080

CMD ["sh", "-c", "exec java -Dserver.port=$SERVER_PORT -Dserver.address=0.0.0.0 -cp app:app/lib/* ru.prodcontest.ProdcontestApplication"]
