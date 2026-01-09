-- ============================================
-- FIX SIGNUP ISSUE - Remove username column
-- ============================================
-- This script fixes the signup error:
-- "Field 'username' doesn't have a default value"
--
-- The User entity no longer has a username field,
-- but the database table still has it as NOT NULL.
-- This script removes the username column.

USE todo_db;

-- Check if username column exists and remove it
-- If you get an error that the column doesn't exist, it's already been removed
ALTER TABLE users DROP COLUMN username;

-- Verify the table structure (should show: id, email, name, password, created_at)
DESCRIBE users;
