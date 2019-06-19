@echo off
set OLD_JAVA_HOME=%JAVA_HOME%
set JAVA_HOME=C:\Program Files\Java\jre1.8.0_74

"%JAVA_HOME%\bin\java.exe" -jar target/spring-batch-demo-1.0.0.jar --spring.config.location=file:target/conf/file_location.properties,target/conf/datasource-cfg.properties,target/conf/unreleased_issue_id_list.info,target/conf/logback.properties,target/conf/sql-format-config.properties

set JAVA_HOME=%OLD_JAVA_HOME%