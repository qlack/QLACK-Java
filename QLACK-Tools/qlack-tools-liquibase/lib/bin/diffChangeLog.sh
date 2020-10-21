#!/usr/bin/env bash

set -e

echo Creating diff database
${DB}-create.sh

echo Populating diff database with existing changelogs
/opt/liquibase/liquibase "$@" \
--driver=$DRIVER \
--classpath=/opt/liquibase/lib/mariadb-java-client.jar:/opt/liquibase/lib/mysql-connector-java.jar \
--url=jdbc:$DB://$DB_HOST:$DB_PORT/qlack_liquibase \
--username=$DB_USER \
--password=$DB_PASS \
--changeLogFile=$CHANGELOG \
--labels="!ignorable" \
update

# Check if the passed CHANGELOG is a file or a directory.
# - In case it is a file, use the file name as provided.
# - In case it is a directory, automatically increment and produce the next filename.
if [[ -d $DIFFLOG ]]; then
  echo "DIFFLOG is a directory. Will try to automatically determine the next changelog's sequence number."
  LAST_FILE_FOUND=$(ls $DIFFLOG |sort |tail -1)
  NEXT_VERSION=$(echo ${LAST_FILE_FOUND} |sed 's/[^0-9]*//g' |sed-incr.sh)
  echo Next version: $NEXT_VERSION
  DIFFLOG="$DIFFLOG/"$(echo ${LAST_FILE_FOUND} | sed -E "s/([a-z]*)([0-9]*)\.([a-z]*)/\1$NEXT_VERSION.\3/g")
fi
set +e

echo Writing diff log in: $DIFFLOG
if [[ ! -z $EXCLUDE_OBJECTS ]]; then
  echo Exclude filter: $EXCLUDE_OBJECTS
fi
/opt/liquibase/liquibase "$@" \
--driver=$DRIVER \
--classpath=/opt/liquibase/lib/mariadb-java-client.jar:/opt/liquibase/lib/mysql-connector-java.jar \
--url=jdbc:$DB://$DB_HOST:$DB_PORT/qlack_liquibase \
--username=$DB_USER \
--password=$DB_PASS \
--changeLogFile=$DIFFLOG \
diffChangeLog \
--referenceUrl=jdbc:$DB://$DB_HOST:$DB_PORT/$DB_SCHEMA \
--referenceUsername=$DB_USER \
--referencePassword=$DB_PASS \
--excludeObjects=$EXCLUDE_OBJECTS

# Check if there are no changes and exit in that case.
CHANGESETS=$(grep -E '<changeSet' -c $DIFFLOG)
if [ $CHANGESETS -eq 0 ]; then
  echo -e "\n"
  echo "----------------------------------"
  echo "NO CHANGES FOUND !!!"
  echo "----------------------------------"
  rm $DIFFLOG
  exit 1
else
  echo "Found $CHANGESETS changes."
fi

set -e
# --changeSetAuthor seems to be ignored, so manually changing this.
echo Modifying author to: $AUTHOR
sed -i "s/author=\".*\" id/author=\"${AUTHOR}\" id/g" $DIFFLOG

# Add logicalFilePath.
FILE=$(echo $DIFFLOG | sed 's:.*/::')
echo Adding logicalFilePath: $FILE
sed -i "s|<databaseChangeLog\(.*\)>|<databaseChangeLog\1 logicalFilePath=\"${FILE}\">|" $DIFFLOG

echo Dropping diff database
${DB}-drop.sh

echo Marking changes as executed.
/opt/liquibase/liquibase "$@" \
--driver=$DRIVER \
--classpath=/opt/liquibase/lib/mariadb-java-client.jar:/opt/liquibase/lib/mysql-connector-java.jar \
--url=jdbc:$DB://$DB_HOST:$DB_PORT/$DB_SCHEMA \
--username=$DB_USER \
--password=$DB_PASS \
--changeLogFile=$CHANGELOG \
changelogSync
