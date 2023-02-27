FROM openjdk:17-alpine
COPY build/libs/requestLimit-0.0.1-SNAPSHOT.jar requestLimit-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/requestLimit-0.0.1-SNAPSHOT.jar"]