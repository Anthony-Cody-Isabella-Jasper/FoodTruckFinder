DROP DATABASE IF EXISTS food_truck_finder_db;
CREATE DATABASE IF NOT EXISTS food_truck_finder_db;
DROP USER IF EXISTS 'ftfuser'@'localhost';
CREATE USER 'ftfuser'@'localhost' IDENTIFIED BY 'codeup';
GRANT ALL ON food_truck_finder_db.* TO 'ftfuser'@'localhost';
USE food_truck_finder_db;