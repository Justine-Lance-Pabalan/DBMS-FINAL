CREATE DATABASE IF NOT EXISTS transportation;

USE transportation;

CREATE TABLE IF NOT EXISTS vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    efficiency DOUBLE NOT NULL,
    battery_capacity DOUBLE NULL,
    emissions_rate DOUBLE NULL
);
