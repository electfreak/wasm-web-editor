FROM gradle:8.5.0-jdk17
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle compileKotlinWasmJs --no-daemon

EXPOSE 8080

ENTRYPOINT ["gradle", "wasmJsBrowserRun"]