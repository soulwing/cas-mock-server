FROM --platform=${BUILDPLATFORM} maven:3.6-jdk-11 AS dep
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -P tomcat


FROM --platform=${BUILDPLATFORM} dep AS tomcat-build
COPY pom.xml .
COPY src ./src
RUN mvn install -P tomcat


FROM --platform=${TARGETPLATFORM} tomcat:8-jdk8-corretto
RUN yum update -y && \
    yum install -y curl ca-certificates && \
    yum clean all && \
    rm -rf /var/cache/yum && \
    rm -rf $CATALINA_HOME/webapps/ROOT
COPY --from=tomcat-build /app/target/*.war $CATALINA_HOME/webapps/ROOT.war

