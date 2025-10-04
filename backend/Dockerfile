FROM eclipse-temurin:21

LABEL org.opencontainers.image.title=FlashciiCards
LABEL org.opencontainers.image.authors="clemnjord"
LABEL org.opencontainers.image.source=https://github.com/clemnjord/flashcii-cards

WORKDIR /app
COPY web/target/*.jar /app/app.jar

EXPOSE 8080
CMD ["java","-jar","/app/app.jar"]