@echo off
echo ============================================
echo Fixing Database Schema for Signup Issue
echo ============================================
echo.
echo This will remove the 'username' column from the users table.
echo.
pause

mysql -u root -p -e "USE todo_db; ALTER TABLE users DROP COLUMN username;"

echo.
echo Database fixed! You can now restart your Spring Boot application.
pause
