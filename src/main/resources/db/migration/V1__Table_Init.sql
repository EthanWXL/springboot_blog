CREATE TABLE USER(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(15) UNIQUE ,
  encoded_password VARCHAR(200) NOT NULL,
  avatar VARCHAR(1000),
  created_at TIMESTAMP,
  updated_at TIMESTAMP);

CREATE TABLE BLOG(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  page BIGINT ,
  title VARCHAR(100),
  description VARCHAR(200),
  content TEXT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP);