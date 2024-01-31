CREATE TABLE IF NOT EXISTS `medications` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(70) NOT NULL UNIQUE,
    `weight` DOUBLE(10,2) NOT NULL,
    `code` VARCHAR(10) NOT NULL UNIQUE,
    `image` VARCHAR(100) DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS `drone_medication` (
    `drone_id` BIGINT NOT NULL,
    `medication_id` BIGINT NOT NULL,
    FOREIGN KEY (`drone_id`) REFERENCES `drones`(`id`),
    FOREIGN KEY (`medication_id`) REFERENCES `medications`(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

