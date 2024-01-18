--jpa_article
INSERT INTO jpa_article (id, content, title) VALUES (1, 'Queen225', 'cascade2');
INSERT INTO jpa_article (id, content, title) VALUES (2, 'election', 'political');

--jpa_comment
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (1, 1, 'Brian6', 'content by Brian6', '2024-01-12 14:50:03', 2);
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (2, 1, 'Queen2', 'Queen225', '2024-01-12 13:33:08', 1);
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (3, 2, 'Wall', 'Kevin', '2024-01-12 13:47:53', 1);
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (4, 1, 'author2', 'content2', '2024-01-12 13:33:08', 1);
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (5, 2, 'JenniferYuan', 'content3', '2024-01-12 13:33:08', 2);
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (6, 2, 'Hsu36', 'Love33', '2024-01-12 13:45:50', 2);
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (7, 1, 'author5', 'content5', '2024-01-12 13:33:08', 1);
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (8, 1, 'author6', 'content6', '2024-01-12 13:33:08', 1);
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (9, 2, 'author7', 'content7', '2024-01-12 13:33:08', 2);
INSERT INTO jpa_comment (id, article_id, author, content, inserted_dt, fr_article_id) VALUES (10, 2, 'author8', 'content8', '2024-01-12 13:33:08', 2);
