-- Check if column exists first
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'product'
      AND COLUMN_NAME = 'size'
);

-- Prepare dynamic SQL
SET @sql = IF(@col_exists = 0, 'ALTER TABLE product ADD COLUMN size INT;', 'SELECT "Column exists"');

-- Execute
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
