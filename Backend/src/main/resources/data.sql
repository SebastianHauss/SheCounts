INSERT INTO article (id, content, author, created_at)
SELECT '49f4c9e9-f8a4-4da6-b854-92a3b4c257bd',
       'article-1.md',
       'Lisa Stein',
       '2024-01-15'
WHERE NOT EXISTS (
    SELECT 1 FROM article WHERE id = '49f4c9e9-f8a4-4da6-b854-92a3b4c257bd'
);

INSERT INTO article (id, content, author, created_at)
SELECT '2b0be6ac-3584-4a01-aec0-5ee9df41e0b7',
       'article-2.md',
       'Martin Schneider',
       '2025-06-20'
WHERE NOT EXISTS (
    SELECT 1 FROM article WHERE id = '2b0be6ac-3584-4a01-aec0-5ee9df41e0b7'
);

INSERT INTO article (id, content, author, created_at)
SELECT '567608d2-47ce-487f-8509-80d30122672e',
       'article-3.md',
       'Lena Müller',
       '2025-01-10'
WHERE NOT EXISTS (
    SELECT 1 FROM article WHERE id = '567608d2-47ce-487f-8509-80d30122672e'
);

INSERT INTO article (id, content, author, created_at)
SELECT '89d9c07d-b318-4658-b3b4-b5f0ba7a60f6',
       'article-4.md',
       'Georg Messinger',
       '2025-09-17'
WHERE NOT EXISTS (
    SELECT 1 FROM article WHERE id = '89d9c07d-b318-4658-b3b4-b5f0ba7a60f6'
);

INSERT INTO article (id, content, author, created_at)
SELECT '732e4a48-48ce-4dd5-b24d-27be976d555e',
       'article-5.md',
       'Laura Schneider',
       '2025-07-02'
WHERE NOT EXISTS (
    SELECT 1 FROM article WHERE id = '732e4a48-48ce-4dd5-b24d-27be976d555e'
);

INSERT INTO article (id, content, author, created_at)
SELECT 'a9ae7d56-7047-4f8e-995c-d298644a37f5',
       'article-6.md',
       'Anna Richter',
       '2025-07-12'
WHERE NOT EXISTS (
    SELECT 1 FROM article WHERE id = 'a9ae7d56-7047-4f8e-995c-d298644a37f5'
);

INSERT INTO article (id, content, author, created_at)
SELECT '59b5462a-d2a4-413e-8491-f5027baf8517',
       'article-7.md',
       'Sophie Berger',
       '2025-07-08'
WHERE NOT EXISTS (
    SELECT 1 FROM article WHERE id = '59b5462a-d2a4-413e-8491-f5027baf8517'
);
