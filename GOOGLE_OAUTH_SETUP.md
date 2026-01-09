# Google OAuth2 Setup Guide

## Step 1: Create Google OAuth2 Credentials

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Navigate to **APIs & Services** > **Credentials**
4. Click **Create Credentials** > **OAuth 2.0 Client ID**
5. If prompted, configure the OAuth consent screen:
   - User Type: **External** (for testing) or **Internal** (for organization)
   - App name: **To-Do List App**
   - User support email: Your email
   - Developer contact: Your email
   - Click **Save and Continue**
   - Scopes: Click **Add or Remove Scopes**, select:
     - `.../auth/userinfo.email`
     - `.../auth/userinfo.profile`
     - `openid`
   - Click **Save and Continue**
   - Test users: Add your email (if using External)
   - Click **Save and Continue**

6. Create OAuth 2.0 Client ID:
   - Application type: **Web application**
   - Name: **To-Do List Backend**
   - Authorized redirect URIs: 
     ```
     http://localhost:8080/api/login/oauth2/code/google
     ```
   - Click **Create**

7. Copy the **Client ID** and **Client Secret**

## Step 2: Configure Backend

1. Open `BACKEND/src/main/resources/application.properties`
2. Replace the placeholders:
   ```properties
   spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
   spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
   ```

## Step 3: Test the Integration

1. Start the backend:
   ```bash
   cd BACKEND
   mvn spring-boot:run
   ```

2. Start the frontend:
   ```bash
   npm run dev
   ```

3. Go to the login page and click **"Sign in with Google"**

## How It Works

1. User clicks "Sign in with Google" button
2. Frontend redirects to: `http://localhost:8080/api/oauth2/authorization/google`
3. Spring Security redirects to Google OAuth consent screen
4. User authorizes the application
5. Google redirects back to: `http://localhost:8080/api/login/oauth2/code/google`
6. `OAuth2SuccessHandler` processes the OAuth response:
   - Extracts user email and name from Google
   - Creates user account if it doesn't exist
   - Generates JWT token
   - Redirects to frontend with token: `http://localhost:5173/auth/callback?token=<jwt>`
7. Frontend `OAuthCallback` component:
   - Extracts token from URL
   - Stores token in localStorage
   - Redirects to dashboard

## Troubleshooting

### "Redirect URI mismatch" error
- Make sure the redirect URI in Google Console exactly matches:
  ```
  http://localhost:8080/api/login/oauth2/code/google
  ```
- Note: No trailing slash!

### "Access blocked" error
- If using External user type, add your email to test users
- Make sure OAuth consent screen is configured

### Token not received
- Check browser console for errors
- Verify backend is running on port 8080
- Check backend logs for OAuth errors

## Security Notes

- OAuth users have a placeholder password: `OAUTH_USER`
- They cannot login with email/password (password is not their Google password)
- They must use Google OAuth to login
- JWT tokens work the same way for both regular and OAuth users
