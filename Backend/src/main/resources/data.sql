INSERT INTO article (id, content, author, created_at)
SELECT '49f4c9e9-f8a4-4da6-b854-92a3b4c257bd',
       'article-1.md',
       'John Doe',
       '2024-01-15' WHERE NOT EXISTS (
    SELECT 1 FROM article WHERE id = '49f4c9e9-f8a4-4da6-b854-92a3b4c257bd'
);