DROP DATABASE IF EXISTS vehicle_list;
CREATE DATABASE IF NOT EXISTS vehicle_list;
DROP USER IF EXISTS 'vehiclelistuser'@'localhost';
CREATE USER 'vehiclelistuser'@'localhost' IDENTIFIED BY 'codeup';
GRANT ALL ON vehicle_list.* TO 'vehiclelistuser'@'localhost';
USE vehicle_list;