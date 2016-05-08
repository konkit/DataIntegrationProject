web: java -Dserver.port=$PORT -jar build/libs/dataintegration-0.0.1-SNAPSHOT.jar
migrate: java -jar target/dependency/liquibase.jar --changeLogFile=src/main/resources/db/changelog/db.changelog-master.yaml --url=$JDBC_DATABASE_URL --classpath=target/dependency/postgres.jar update

