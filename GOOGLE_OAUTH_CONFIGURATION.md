# Google OAuth2 Configuration - Exact Values

## Authorized JavaScript Origins
**For use with requests from a browser**

Add these URLs (one per line):
```
http://localhost:5173
http://localhost:3000
```

**Why:**
- These are your frontend development URLs
- Allows the frontend to initiate OAuth requests
- `localhost:5173` is Vite default port
- `localhost:3000` is alternative React port

## Authorized Redirect URIs
**For use with requests from a web server**

Add this URL (exactly as shown):
```
http://localhost:8080/api/login/oauth2/code/google
```

**Why:**
- This is where Google redirects after authentication
- Must match exactly what Spring Security expects
- Format: `http://localhost:8080` (backend) + `/api` (context path) + `/login/oauth2/code/google` (OAuth endpoint)

## Complete Configuration Steps

### 1. In Google Cloud Console:

1. Go to **APIs & Services** > **Credentials**
2. Click on your **OAuth 2.0 Client ID**
3. Under **Authorized JavaScript origins**, click **+ ADD URI**:
   ```
   http://localhost:5173
   ```
   Then add:
   ```
   http://localhost:3000
   ```

4. Under **Authorized redirect URIs**, click **+ ADD URI**:
   ```
   http://localhost:8080/api/login/oauth2/code/google
   ```

5. Click **SAVE**

### 2. Important Notes:

⚠️ **Exact Match Required:**
- The redirect URI must match EXACTLY
- No trailing slashes
- Case sensitive
- Must include `/api` (your Spring Boot context path)

⚠️ **Port Numbers:**
- `5173` = Frontend (Vite default)
- `8080` = Backend (Spring Boot default)
- If you change ports, update these URLs accordingly

⚠️ **Production Setup:**
When deploying to production, you'll need to add:
- **JavaScript Origins:** `https://yourdomain.com`
- **Redirect URIs:** `https://yourdomain.com/api/login/oauth2/code/google`

### 3. Verification:

After saving, test the flow:
1. Start backend: `cd BACKEND && mvn spring-boot:run`
2. Start frontend: `npm run dev`
3. Go to login page
4. Click "Sign in with Google"
5. Should redirect to Google login
6. After authorization, should redirect back to your app

### 4. Common Errors:

**"Redirect URI mismatch"**
- Check redirect URI matches exactly
- No trailing slash
- Correct port number
- Includes `/api` context path

**"Origin mismatch"**
- Check JavaScript origins include frontend URL
- Must be `http://localhost:5173` (not `https://`)

**"Settings not taking effect"**
- Wait 5 minutes to a few hours (as Google mentioned)
- Clear browser cache
- Try incognito mode

## Summary

**Authorized JavaScript Origins:**
```
http://localhost:5173
http://localhost:3000
```

**Authorized Redirect URIs:**
```
http://localhost:8080/api/login/oauth2/code/google
```

That's it! Save and wait a few minutes for changes to propagate.
