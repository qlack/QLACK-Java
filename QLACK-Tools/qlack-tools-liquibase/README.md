# QLACK tools - Liquibase
This is a little tool, based on a Docker container, allowing to automatically 
detect changes in your database and export detected changes to 
Liquibase _changesets_. 

[![QLACK Tools liquibase](http://img.youtube.com/vi/K08foBXwo_Y/0.jpg)](http://www.youtube.com/watch?v=K08foBXwo_Y "QLACK Tools liquibase")

## Preface
This QLACK tool tries to provide solutions to some usual development use cases
pertaining to the evolution of your database schema in your project.

The first thing it does is allowing you to produce an initial Liquibase changeset
capturing the current state of your schema. This should be, usually, used in the
beginning of your project once you have prepared the initial version of
your schema.

The second thing it does is producing liquibase changesets capturing changes
on the above schema. This allows you to keep changing your schema using the tools
you know rather than having to perform all changes via manually-crafted
Liquibase changesets.

Please make sure you read the _Word of caution_ section before you proceed.

### Word of caution
TLDR; Always review the automatically-produced changeset!

* It is suggested to use Liquibase from the beginning of your project
and capture all database changes in manually crafted changesets. This provides
you the greatest flexibility and control in how your schema evolves.
* Make sure you **always** meticulously review the produced diff-changeset
before you include it in your project. Liquibase does a great job in detecting
changes, however there are logical changes that can not be detected. For example,
if you rename a column, Liquibase will produce _drop column_ and  _add column_
statements - most probably not what you need/expect as you will lose all
data of the original column. In such cases, delete the offending changesets
and replace them with manually-crafted ones (for the rename column example,
you can simply create a [rename column](https://www.liquibase.org/documentation/changes/rename_column.html)
changeset).
* Do not want to take our own word of caution? [Fine](http://www.liquibase.org/2007/06/the-problem-with-database-diffs.html).

## Building the container
`eurodyn/qlack-tools-liquibase` container is already available in Docker Hub,
however if you want to build it locally you can execute:

`make build`

If you do not have `make` available, you can alternatively build it with:

`docker build . -t eurodyn/qlack-tools-liquibase`

## Compatible databases
Currently the following databases are supported:
* MySQL
* MariaDB

Note that adding support for new databases is relatively easy, so we would
wholeheartedly welcome PRs for that.

## Prerequisites
You need to have access to the database server from the host where your
Docker Engine runs. To produce diffs, you need a user with appropriate
privileges to create new databases (a temporary database/schema needs to
be created which is then dropped at the end).

## Producing the initial changelog
To produce the initial changelog for your database you need to pass the
following parameters as environmental variables to the Docker container:

`DRIVER`: The class of the JDBC driver to connect to your database.

`DB_HOST`: The host running your database server. In case you run the
database server and Docker Engine on your own machine, you can reference
the host machine from within the container using special Docker DNS entries
such as docker.for.mac.localhost, docker.for.windows.localhost, etc.

`DB_PORT`: The port where your database server listens to.

`DB_SCHEMA`: The name of the database you are accessing.

`DB`: The type/name of the database to connect to (currently, mariadb, mysql).

`DB_USER`: The username to connect with to your database.

`DB_PASS`: The password to connect with to your database.

In addition you should mount a local directory into the Docker container, so
that the changelog can be written into. This directory should be mounted under
`/data`. The generated changelog will be named as `db.changelog.xml`.

For example:
```
docker run --rm \
-e DRIVER=org.mariadb.jdbc.Driver \
-e DB_HOST=docker.for.mac.localhost \
-e DB_PORT=45000 \
-e DB_SCHEMA=sample1 \
-e DB=mariadb \
-e DB_USER=root \
-e DB_PASS=root \
-v /Users/jdoe/tmp:/data \
eurodyn/qlack-tools-liquibase \
generateChangeLog.sh
```

## Producing diffs
To capture changes in your database this script goes through a series of
steps. First, it creates a temporary database in the database server you
have indicated. The temporary database is populated using the Liquibase
scripts you are already using in your project. Finally, the temporary
database is compared with the database you are actively using (on
which you have performed changes) and a changelog is produced.
To produce the diff changelog for your database you need to pass the
following parameters as environmental variables to the Docker container:

`DRIVER`: The class of the JDBC driver to connect to your database.

`DB_HOST`: The host running your database server. In case you run the
database server and Docker Engine on your own machine, you can reference
the host machine from within the container using special Docker DNS entries
such as docker.for.mac.localhost, docker.for.windows.localhost, etc.

`DB_PORT`: The port where your database server listens to.

`DB_SCHEMA`: The name of the database you are accessing.

`DB`: The type/name of the database to connect to (currently, mariadb, mysql).

`DB_USER`: The username to connect with to your database.

`DB_PASS`: The password to connect with to your database.

`CHANGELOG`: The changelog of your project, so that Liquibase scripts
can be replied from it.

`AUTHOR`: The author of the changelog.

`EXCLUDE_OBJECTS`: A pattern for objects to exclude from diffing 
(as per [Liquibase](http://www.liquibase.org/2015/01/liquibase-3-3-2-released.html)).

Caveat #1: In case your changelog incorporates
includes, make sure that these are referenced in a way that can be found
in all environments. For example, since Liquibase automatically looks
for entries in your classpath, you can reference all your includes using
an absolute style, e.g. _/db/changelog/changes/script0001.xml_. This will
allow Liquibase to find your entries under the _resources_ folder of your
application as well as by this script.

Caveat #2: Make sure all your changelogs have the _logicalFilePath_ attribute.
The _logicalFilePath_ can simply be the filename of your script and it is
necessary for this script to work correctly.

`DIFFLOG`: The name of the changelog that will be produced. Make sure
you provide a path to a folder which is mount in your Docker container,
so that the script is actually copied to your host running the Docker Engine.

TIP: By specifying a folder instead of a file, you can let this script
to try automatically detecting the next-version number of your Liquibase
changelogs. The script looks for a pattern of _prefixnumber.someextension_,
for example, myproject_0001.xml, dbupdate0001.xml, etc.

Do not forget to mount on the Docker container the folder with the changesets 
of your project.

Example:
```
docker run --rm \
-e DRIVER=org.mariadb.jdbc.Driver \
-e DB_HOST=docker.for.mac.localhost \
-e DB_PORT=45000 \
-e DB_SCHEMA=sample1 \
-e DB=mariadb \
-e DB_USER=root \
-e DB_PASS=root \
-e CHANGELOG=/db/changelog/db.changelog-master.yaml \
-e DIFFLOG=/db/changelog/changes/difflog.xml \
-e AUTHOR="John Doe" \
-v /Users/jdoe/Projects/proj1/src/main/resources/db:/db \
eurodyn/qlack-tools-liquibase \
diffChangeLog.sh
```

## FAQ
### In my project's changelog I reference other/external changelogs. How do I include those?

You can include external changelogs the same way you include the changelogs
of your own project by mounting them in the Docker container. However,
if you have many external references this may become tedious. Since most
probably you do not need those external changes when trying to capture the changes
of your own-maintained schema, an alternative approach is to maintain 
two different changelogs in your project: 
One with the external references and another without; you can use the latter
with this tool to ignore external changelogs.

### I followed the approach of maintaining two changelogs to separate external changesets, however in my own changelog I have changesets inserting data into the tables produced by the external changelog I am ignoring. Catch-22?

This is a common use case which you can handle with some preparation. For example, let us say that in your project you are
using the wonderful [QLACK Fuse Settings](https://github.com/eurodyn/QLACK/tree/master/QLACK-Fuse/qlack-fuse-settings) module.
In your changelogs you have a changeset that inserts some default data into QLACK Fuse Settings, for example, regarding the
system's temporary folder, with a changeset similar to:

```
<changeSet id="change_0349" author="European Dynamics">
    <sql>
      INSERT INTO set_setting(`id`, `owner`, `group_name`, `key_name`, `val`, `created_on`, `sensitivity`, `dbversion`,  `psswrd`)
      VALUES ('248175bd-1b3e-44c9-b057-e8ede7a113a3', 'System', 'System', 'tempFolder', '/tmp', now(), 0, 0, 0);
    </sql>
</changeSet>
```

If in your changelog you are excluding external changelogs the above changeset 
will, obviously, fail. However, do you really need this changeset to be 
executed in the context of capturing database changes? Probably you do 
not as it does not provide anything related to the structure of your 
database (which is what you aim to automatically capture). We therefore
need a way to tell liquibase to exclude all such changesets - this is
easily done by using [Liquibase labels](https://www.liquibase.org/2014/11/contexts-vs-labels.html).

Label all your scripts inserting data with a specific
label, so when this tool is running it knows to ignore them. The default
label used is **`ignorable`**. So, the above changeset should be effectively
written as:

```
<changeSet id="change_0349" author="European Dynamics" labels="ignorable">
    <sql>
      INSERT INTO set_setting(`id`, `owner`, `group_name`, `key_name`, `val`, `created_on`, `sensitivity`, `dbversion`,  `psswrd`)
      VALUES ('248175bd-1b3e-44c9-b057-e8ede7a113a3', 'System', 'System', 'tempFolder', '/tmp', now(), 0, 0, 0);
    </sql>
</changeSet>
```

OK, you are nearly there. 

The last bit you still need to configure is to
tell this tool to ignore the tables of your schema generated by the changelogs
you ignore (otherwise, those tables will be detected as new). This can be easily
done by specifying an [excludeObjects](http://www.liquibase.org/2015/01/liquibase-3-3-2-released.html) 
pattern as a parameter to the diff script of this tool.

To recap:
- Maintain and use this tool with a separate changelog with no references to external changelogs.
- Mark your changesets containing interactions with the tables ignored above with ``ignorable`` label.
- Provide an ``excludeObjects`` parameter to exclude ignored tables from diff.