# How to Run the To-Do List Application

## Prerequisites
1. MySQL Server running on localhost:3306
2. Java 17+ installed
3. Maven installed
4. Node.js and npm installed (for frontend)

## Step 1: Fix Database Schema (REQUIRED)

**IMPORTANT:** You must remove the `username` column from the database before running the application.

### Option A: Using MySQL Command Line
```bash
mysql -u root -p
```
Then run:
```sql
USE todo_db;
ALTER TABLE users DROP COLUMN username;
EXIT;
```

### Option B: Using MySQL Workbench
1. Open MySQL Workbench
2. Connect to your database
3. Run this SQL:
```sql
USE todo_db;
ALTER TABLE users DROP COLUMN username;
```

### Option C: Using the SQL file
```bash
mysql -u root -p < BACKEND/database/FIX_SIGNUP_ISSUE.sql
```

## Step 2: Start the Backend

Open a terminal in the project root and run:

```bash
cd BACKEND
mvn spring-boot:run
```

The backend will start on: **http://localhost:8080/api**

Wait for the message: "Started ToDoListApplication"

## Step 3: Start the Frontend (Optional)

Open a **new terminal** in the project root and run:

```bash
npm install
npm run dev
```

The frontend will start on: **http://localhost:5173**

## Step 4: Access the Application

1. Open your browser and go to: **http://localhost:5173**
2. Sign up with a new account
3. Login and start creating tasks!

## Troubleshooting

### Port 8080 already in use
If you get "Port 8080 was already in use":
```bash
# Find the process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with the actual process ID)
taskkill /PID <PID> /F
```

### Database connection error
- Make sure MySQL is running
- Check the database credentials in `BACKEND/src/main/resources/application.properties`
- Verify the database `todo_db` exists

### Signup still not working
- Make sure you ran Step 1 (removed username column)
- Check the backend logs for errors
- Verify the database table structure: `DESCRIBE todo_db.users;`
