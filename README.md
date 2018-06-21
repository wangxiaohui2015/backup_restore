# Backup Restore System

## Description
This is a backup restore system with dedup ability.

## Setup environment
### Prerequisites
1. Install JDK 1.8.x  (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and setup JAVA_HOME environment variable.
2. Install Maven 3.5.x (http://maven.apache.org/download.cgi) and setup PATH environment variable.
3. Install MySQL 5.7.x (https://www.mysql.com/downloads/)

### Building
``` shell
git clone git@cncdgitlab.ccoe.lab.emc.com:wangs56/backup_restore.git
cd backup_restore
mvn clean package
```
### Initialize DB
Create DB using script backup and restore.sql.

### Deployment
You can just use backup_restore.zip to have a try, or build BackRestore-1.0.0-RELEASE-jar-with-dependencies.jar and replace related jar in zip file.
Create folder "backupdata" in current directory and then double click start.bat to launch main console.

## Configurations
### Update DB info in conf.properties
db.url=jdbc\:mysql\://localhost\:3306/backupdb?useUnicode\=true&amp;characterEncoding\=UTF-8&amp;
db.username=root
db.password=root
db.driver=com.mysql.jdbc.Driver

No need to change item: backup.targetDir=
