CREATE TABLE USER(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(15) UNIQUE ,
  encoded_password VARCHAR(200) NOT NULL,
  avatar VARCHAR(1000),
  created_at TIMESTAMP,
  updated_at TIMESTAMP)