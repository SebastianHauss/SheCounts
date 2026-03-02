INSERT INTO article (id, content, author, created_at)
SELECT '49f4c9e9-f8a4-4da6-b854-92a3b4c257bd',
       'article-1.md',
       'Lisa Stein',
       '2024-01-15' WHERE NOT EXISTS (
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