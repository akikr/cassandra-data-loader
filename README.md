# This is a cassandra data loader application

## To run this application follow these steps:
 * Step 1: 
   * Put the `better-reads-data-loader.yml` at location path define in `CONFIG_FILE_LOCATION` member variable of [CassandraDataLoaderApplication]() class with all the configs properties needed as described in `application.yml`
 
 * Step 2:
   * Put the `data-dump-path-for-author-data` & `data-dump-path-for-books-data` in the `datadump.location.author` & `datadump.location.works` arguments respectively in [better-reads-data-loader.sh]() file
 
 * Step 3:
   * Now run the application using `better-reads-data-loader.sh` file as:
   ```
   [better-reads-data-loader]$ sh bin/better-reads-data-loader.sh
   ```
