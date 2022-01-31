FROM openjdk:17
ADD target/payment-service-exec.jar payment-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "payment-service.jar"]