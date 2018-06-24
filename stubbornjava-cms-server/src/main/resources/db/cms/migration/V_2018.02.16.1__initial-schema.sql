CREATE TABLE IF NOT EXISTS app (
  app_id INT NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  date_created_ts DATETIME NOT NULL,
  PRIMARY KEY (app_id),
  UNIQUE KEY `name_idx` (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into app (name, date_created_ts) values ('stubbornjava', now());

CREATE TABLE IF NOT EXISTS user (
  user_id BIGINT NOT NULL AUTO_INCREMENT,
  email_hash char(32) NOT NULL,
  email varchar(1024) DEFAULT NULL,
  active boolean NOT NULL,
  date_created_ts DATETIME NOT NULL,
  date_updated_ts DATETIME NOT NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY `email_hash_idx` (email_hash)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into user (email_hash, email, active, date_created_ts, date_updated_ts) values (md5('bill@dartalley.com'), 'bill@dartalley.com', true, now(), now());

CREATE TABLE IF NOT EXISTS post_tag (
    post_tag_id INT NOT NULL AUTO_INCREMENT,
    app_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    last_update_ts DATETIME NOT NULL,
    PRIMARY KEY (post_tag_id),
    UNIQUE KEY `app_id_name_unique` (app_id, name),
    CONSTRAINT `post_tag_app_id_fk` FOREIGN KEY (`app_id`) REFERENCES `app` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS post (
    post_id BIGINT NOT NULL AUTO_INCREMENT,
    app_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    metaDesc VARCHAR(1024) NOT NULL,
    draft_status varchar(255) NOT NULL,
    last_update_ts DATETIME NOT NULL,
    date_created_ts DATETIME NOT NULL,
    date_created DATE NOT NULL,
    content_template MEDIUMTEXT DEFAULT NULL,
    PRIMARY KEY (post_id),
    UNIQUE KEY `app_id_slug` (app_id, slug),
    KEY `date_created_idx` (date_created),
    CONSTRAINT `post_app_id_fk` FOREIGN KEY (`app_id`) REFERENCES `app` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS post_tag_links (
    post_id BIGINT NOT NULL,
    post_tag_id INT NOT NULL,
    PRIMARY KEY (post_id, post_tag_id),
    CONSTRAINT `post_tag_links_post_id_fk` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`),
    CONSTRAINT `post_tag_links_post_tag_id_fk` FOREIGN KEY (`post_tag_id`) REFERENCES `post_tag` (`post_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
