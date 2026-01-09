# OAuth Callback Debugging Guide

## Problem
Stuck on "Processing authentication..." screen, not redirecting to dashboard.

## Debugging Steps

### 1. Check Browser Console
Open browser DevTools (F12) and check the Console tab. You should see:
- `OAuth Callback - Token: <token or null>`
- `OAuth Callback - Error: <error or null>`
- `OAuth Callback - All params: {...}`

### 2. Check Browser URL
Look at the URL bar. It should be:
```
http://localhost:5173/auth/callback?token=<long-jwt-token>
```

If you see:
- No `?token=` parameter → Backend redirect failed
- `?error=` parameter → OAuth error occurred
- Just `/auth/callback` with no params → Redirect didn't include token

### 3. Check Backend Logs
Look for these log messages:
```
OAuth2SuccessHandler: User authenticated - <email>
OAuth2SuccessHandler: Token generated - Yes
OAuth2SuccessHandler: Token length - <number>
OAuth2SuccessHandler: Redirecting to - http://localhost:5173/auth/callback?token=...
```

### 4. Check Network Tab
1. Open DevTools → Network tab
2. Look for the redirect request
3. Check if the redirect URL includes the token parameter

## Common Issues

### Issue 1: Token Not in URL
**Symptoms:** URL is `http://localhost:5173/auth/callback` with no token

**Possible Causes:**
- Backend redirect failed
- Token too long (URL length limit)
- Redirect URL encoding issue

**Solution:**
- Check backend logs for errors
- Try using `window.location.href` instead of redirect
- Check if token is being generated

### Issue 2: CORS Error
**Symptoms:** Console shows CORS error

**Solution:**
- Verify CORS configuration in SecurityConfig
- Check if frontend URL matches allowed origins

### Issue 3: Token Invalid
**Symptoms:** Token received but login fails

**Solution:**
- Check if token is being stored in localStorage
- Verify JWT secret matches
- Check token expiration

## Quick Fixes

### Fix 1: Add Manual Redirect Button
Already added - user can click "Go back to login" if stuck

### Fix 2: Check Token in URL
```javascript
// In browser console, run:
new URLSearchParams(window.location.search).get('token')
```

### Fix 3: Manual Token Storage
If token is in URL but not being stored:
```javascript
// In browser console:
const token = new URLSearchParams(window.location.search).get('token');
if (token) {
  localStorage.setItem('jwt_token', token);
  window.location.href = '/dashboard';
}
```

## Expected Flow

1. ✅ User clicks "Sign in with Google"
2. ✅ Redirects to Google OAuth
3. ✅ User authorizes
4. ✅ Google redirects to: `http://localhost:8080/api/login/oauth2/code/google`
5. ✅ OAuth2SuccessHandler processes
6. ✅ Redirects to: `http://localhost:5173/auth/callback?token=<jwt>`
7. ✅ OAuthCallback extracts token
8. ✅ Stores in localStorage
9. ✅ Redirects to dashboard

## Next Steps

1. **Check browser console** - Look for the debug logs
2. **Check backend logs** - Look for OAuth2SuccessHandler messages
3. **Check URL** - Verify token is in the URL
4. **Report findings** - Share what you see in console/logs

The debugging code will help identify where the flow is breaking!
