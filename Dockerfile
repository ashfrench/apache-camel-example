FROM java:8


COPY target/*.jar /usr/myapp/app.jar
WORKDIR /usr/myapp/

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]