CREATE TABLE BLOG(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  page BIGINT ,
  title VARCHAR(100),
  description VARCHAR(200),
  content TEXT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP)