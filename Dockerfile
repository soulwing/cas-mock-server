FROM maven:3.5-jdk-8-alpine AS dep
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -P tomcat


FROM dep AS tomcat-build
COPY pom.xml .
COPY src ./src
RUN mvn install -P tomcat


FROM tomcat:8.0-jre8-alpine
RUN rm -r $CATALINA_HOME/webapps/ROOT
COPY --from=tomcat-build /app/target/*.war $CATALINA_HOME/webapps/ROOT.war

