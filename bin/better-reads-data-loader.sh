#!/bin/bash

# Running the better-reads-data-loader application using CLI and providing the data-dumb authors & books data paths
# via spring-run-time arguments to run multiple instance of data-loader-application simultaneously:-
./mvnw clean install -DskipTests;./mvnw spring-boot:run -Dspring-boot.run.arguments="--datadump.location.author=<data-dump-path-1-for-author-data> --datadump.location.works=<data-dump-path-1-for-books-data>" &
/mvnw clean install -DskipTests;./mvnw spring-boot:run -Dspring-boot.run.arguments="--datadump.location.author=<data-dump-path-2-for-author-data> --datadump.location.works=<data-dump-path-2-for-books-data>"
