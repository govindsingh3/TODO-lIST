-- Remove username column from users table
-- This script fixes the issue where the database still has a username column
-- but the application code no longer uses it

USE todo_db;

-- Check if the column exists before dropping it
-- MySQL doesn't support IF EXISTS for DROP COLUMN in older versions
-- If you get an error that the column doesn't exist, it's already been removed
ALTER TABLE users DROP COLUMN username;
