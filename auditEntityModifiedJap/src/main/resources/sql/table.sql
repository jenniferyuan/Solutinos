DROP TABLE IF EXISTS jpa_comment;
DROP TABLE IF EXISTS jpa_article;
DROP TABLE IF EXISTS emp_audit;

CREATE TABLE jpa_article(
	id int(11) not null Auto_increment,
	title varchar(50) not null,
	content longtext,
	primary key(id)       
) ENGINE=INNODB DEFAULT CHARSET=utf8;
    
CREATE TABLE jpa_comment(
	id int(11) not null Auto_increment,
	article_id int(11) not null,
	content longtext,
	author varchar(20) default null,
	inserted_dt timestamp not null,
	fr_article_id INT,
	primary key(id)       
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE emp_audit
(
	emp_audit_id INT NOT NULL AUTO_INCREMENT,
	table_name VARCHAR(50),
	pkey_field_value INT,
	field_name VARCHAR(50),
	old_value VARCHAR(50),
	new_value VARCHAR(50),
	inserted_by VARCHAR(25) NOT NULL,
	inserted_dt TIMESTAMP NOT NULL,
	PRIMARY KEY (emp_audit_id)
);