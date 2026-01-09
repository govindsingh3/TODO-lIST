# Google OAuth User Data Flow & Task Management

## ✅ Yes, Google OAuth Users ARE Saved in Database!

### How User Data is Saved

When a user logs in via Google OAuth:

1. **OAuth2SuccessHandler** processes the Google authentication
2. **Checks if user exists** in database by email:
   ```java
   User user = userRepository.findByEmail(userEmail)
   ```
3. **If user doesn't exist**, creates and saves new user:
   ```java
   User newUser = User.builder()
       .email(userEmail)           // From Google account
       .name(userName)             // From Google account
       .password("OAUTH_USER")     // Placeholder
       .build();
   userRepository.save(newUser);   // ✅ SAVED TO DATABASE
   ```
4. **If user exists**, retrieves existing user from database
5. **Generates JWT token** containing the user's email
6. **User is now in database** with all their information

### Database Structure

**Users Table:**
```
id | email              | name      | password      | created_at
---|--------------------|-----------|---------------|------------
1  | user@gmail.com     | John Doe  | OAUTH_USER    | 2026-01-09
2  | regular@email.com  | Jane      | $2a$10$...    | 2026-01-09
```

**Tasks Table:**
```
id | task_description | priority | status | deadline | user_id | created_at
---|------------------|----------|--------|----------|---------|------------
1  | Buy groceries    | URGENT   | DONE   | ...      | 1       | 2026-01-09
2  | Finish project   | NORMAL   | ...    | ...      | 1       | 2026-01-09
```

## How Tasks Work for OAuth Users

### 1. Creating a Task

**Flow:**
```
User clicks "Add Task" 
  → Frontend sends request with JWT token
  → TaskService.createTask() receives request
  → getCurrentUser() extracts email from JWT token
  → Finds user in database by email
  → Creates task with user_id = user.getId()
  → Task saved to database ✅
```

**Code:**
```java
public TaskDTO createTask(CreateTaskRequest request) {
    User user = getCurrentUser();  // Gets user from JWT token
    Task task = Task.builder()
        .taskDescription(request.getTaskDescription())
        .priority(request.getPriority())
        .status(request.getStatus())
        .deadline(request.getDeadline())
        .user(user)  // ✅ Task linked to user
        .build();
    Task savedTask = taskRepository.save(task);  // ✅ Saved to database
    return TaskDTO.fromEntity(savedTask);
}
```

### 2. Viewing Tasks

**Flow:**
```
User opens dashboard
  → Frontend sends GET /tasks with JWT token
  → TaskService.getAllTasks() receives request
  → getCurrentUser() extracts email from JWT token
  → Finds user in database by email
  → Queries tasks WHERE user_id = user.getId()
  → Returns only that user's tasks ✅
```

**Code:**
```java
public List<TaskDTO> getAllTasks(...) {
    User user = getCurrentUser();  // Gets user from JWT token
    List<Task> tasks = taskRepository.findByUserId(user.getId());
    // Returns only tasks for this user ✅
    return sortTasks(tasks, sortBy, sortOrder);
}
```

### 3. Updating/Deleting Tasks

**Flow:**
```
User updates/deletes task
  → TaskService validates ownership
  → Checks if task.user_id == currentUser.getId()
  → Only allows if user owns the task ✅
```

**Code:**
```java
private void validateUserOwnership(Task task) {
    User currentUser = getCurrentUser();
    if (!task.getUser().getId().equals(currentUser.getId())) {
        throw new RuntimeException("Unauthorized access to task");
    }
}
```

## Complete User Journey

### First Time Google Login:
1. User clicks "Sign in with Google"
2. Authorizes on Google
3. **New user created in database** with:
   - Email from Google
   - Name from Google
   - Password: "OAUTH_USER"
   - Created timestamp
4. JWT token generated
5. User redirected to dashboard

### Subsequent Logins:
1. User clicks "Sign in with Google"
2. Authorizes on Google
3. **Existing user found in database** by email
4. JWT token generated
5. User redirected to dashboard

### Creating Tasks:
1. User creates task
2. **Task saved with user_id** linking to user
3. Task appears in user's task list

### Viewing Tasks Later:
1. User logs in (Google or regular)
2. **All their tasks loaded** from database
3. Tasks filtered by user_id automatically

## Key Points

✅ **OAuth users ARE saved to database** - Same as regular users
✅ **Tasks ARE saved with user_id** - Linked to the user
✅ **Tasks persist across sessions** - Stored in database
✅ **User can view tasks later** - Retrieved by user_id
✅ **Same functionality** - OAuth users work exactly like regular users

## Database Relationships

```
User (1) ────────< (Many) Tasks
  │                      │
  │                      │
  id                  user_id (foreign key)
```

- One user can have many tasks
- Each task belongs to one user
- Tasks are retrieved by user_id
- User ownership is validated on all operations

## Summary

**Everything works the same for OAuth users!**

- ✅ User data saved to database
- ✅ Tasks saved with user_id
- ✅ Tasks persist across logins
- ✅ User can view/edit/delete their tasks
- ✅ Complete data persistence

The only difference is OAuth users have a placeholder password ("OAUTH_USER") and must use Google to login, but all task functionality works identically!
