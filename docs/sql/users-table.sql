-- 用户表建表脚本，对应实体 com.unirun.runner.entity.UserProfile
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  open_id VARCHAR(64) NOT NULL UNIQUE,
  nickname VARCHAR(64) NOT NULL,
  avatar_url VARCHAR(255),
  gender VARCHAR(16) DEFAULT 'unknown',
  country VARCHAR(64),
  province VARCHAR(64),
  city VARCHAR(64),
  school VARCHAR(64),
  class_name VARCHAR(64),
  voice_enabled TINYINT(1) NOT NULL DEFAULT 1,
  last_login_at DATETIME,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT idx_users_open_id UNIQUE (open_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
