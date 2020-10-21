# Base file to create a liquibase container with various database client-drivers included.
FROM openjdk:8u181-jre-stretch

ENV LIQUIBASE_VERSION 3.8.9
ENV SLF4J_VERSION 1.7.25
ENV MARIADB_CLIENT_VERSION 2.3.0
ENV MYSQL_CLIENT_VERSION 8.0.22

RUN \
  apt-get update && \
  apt-get install -y mysql-client && \
  wget https://github.com/liquibase/liquibase/releases/download/v${LIQUIBASE_VERSION}/liquibase-${LIQUIBASE_VERSION}.tar.gz -O /tmp/liquibase.tar.gz && \
  mkdir /opt/liquibase && \
  tar xzf /tmp/liquibase.tar.gz -C /opt/liquibase && \
  wget http://maven.org/maven2/org/slf4j/slf4j-api/${SLF4J_VERSION}/slf4j-api-${SLF4J_VERSION}.jar -O /opt/liquibase/lib/slf4j-api-${SLF4J_VERSION}.jar && \
  wget https://downloads.mariadb.com/Connectors/java/connector-java-${MARIADB_CLIENT_VERSION}/mariadb-java-client-${MARIADB_CLIENT_VERSION}.jar -O /opt/liquibase/lib/mariadb-java-client.jar && \
  wget http://maven.org/maven2/mysql/mysql-connector-java/${MYSQL_CLIENT_VERSION}/mysql-connector-java-${MYSQL_CLIENT_VERSION}.jar -O /opt/liquibase/lib/mysql-connector-java.jar

COPY lib/bin/* /usr/local/bin/
