FROM openjdk:17
ADD target/movie-service-exec.jar movie-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "movie-service.jar"]