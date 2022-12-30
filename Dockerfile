FROM azul/zulu-openjdk-alpine:13-jre

EXPOSE 5500

COPY target/springBoot-kurs-0.0.1-SNAPSHOT.jar back.jar

CMD ["java","-jar","/back.jar"]