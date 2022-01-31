FROM openjdk:17
ADD target/order-service-exec.jar order-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "order-service.jar"]