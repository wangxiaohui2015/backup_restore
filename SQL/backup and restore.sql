DROP DATABASE IF EXISTS backupdb;
CREATE DATABASE backupdb;

USE backupdb;

DROP TABLE IF EXISTS policy;
CREATE TABLE policy (
id INT PRIMARY KEY AUTO_INCREMENT,
policy_name VARCHAR(1024),
source_dir VARCHAR(1024),
memo VARCHAR(10240)
) ENGINE=INNODB CHARSET=UTF8;

DROP TABLE IF EXISTS backup_info;
CREATE TABLE backup_info (
id INT PRIMARY KEY AUTO_INCREMENT,
policy_id INT NOT NULL,
backup_type INT,
start_time DATETIME,
end_time DATETIME,
target_dir VARCHAR(1024),
is_successful INT,
file_size long,
dedup_size long,
dedup_rate double,
FOREIGN KEY (policy_id) REFERENCES policy(id)
) ENGINE=INNODB CHARSET=UTF8;

DROP TABLE IF EXISTS meta_data_catalog;
CREATE TABLE meta_data_catalog (
id INT PRIMARY KEY AUTO_INCREMENT,
meta_data_ids VARCHAR(100000),
parent_id INT DEFAULT 0
) ENGINE=INNODB CHARSET=UTF8;

DROP TABLE IF EXISTS meta_data;
CREATE TABLE meta_data (
id INT PRIMARY KEY AUTO_INCREMENT,
digest VARCHAR(128) NOT NULL,
file_path VARCHAR(64) NOT NULL,
INDEX meta_data_digest(digest)
) ENGINE=INNODB CHARSET=UTF8; 

DROP TABLE IF EXISTS backup_details;
CREATE TABLE backup_details (
id INT PRIMARY KEY AUTO_INCREMENT,
backup_info_id INT NOT NULL,
file_name VARCHAR(1204),
is_dir INT DEFAULT 0,
parent_id INT,
meta_data_catalog_root_id INT NOT NULL,
FOREIGN KEY (backup_info_id) REFERENCES backup_info(id)
) ENGINE=INNODB CHARSET=UTF8;

