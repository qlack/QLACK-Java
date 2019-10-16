#!/usr/bin/env bash

/opt/liquibase/liquibase \
--driver=$DRIVER \
--classpath=/opt/liquibase/lib/mariadb-java-client.jar \
--url=jdbc:$DB://$DB_HOST:$DB_PORT/$DB_SCHEMA \
--username=$DB_USER \
--password=$DB_PASS \
--changeLogFile=/data/db.changelog.xml \
generateChangeLog