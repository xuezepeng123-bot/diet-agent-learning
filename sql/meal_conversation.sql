CREATE TABLE IF NOT EXISTS meal_conversation (
    session_id CHAR(36) NOT NULL PRIMARY KEY,
    meal_time VARCHAR(20) NULL,
    taste VARCHAR(20) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;