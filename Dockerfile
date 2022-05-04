FROM scsb-base as builder
WORKDIR application
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} scsb-auth.jar
RUN java -Djarmode=layertools -jar scsb-auth.jar extract

FROM scsb-base

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/scsb-auth.jar/ ./
ENTRYPOINT java -jar -Denvironment=$ENV scsb-auth.jar && bash
