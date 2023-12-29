FROM gradle:8.5.0-jdk17
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build --no-daemon

EXPOSE 8082

ENTRYPOINT ["gradle", "run"]