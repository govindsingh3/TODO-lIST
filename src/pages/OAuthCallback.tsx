import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { CheckCircle, AlertCircle } from 'lucide-react';

const OAuthCallback: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { login } = useAuth();
  const token = searchParams.get('token');
  const error = searchParams.get('error');

  useEffect(() => {
    // Debug: Log URL parameters
    console.log('OAuth Callback - Token:', token);
    console.log('OAuth Callback - Error:', error);
    console.log('OAuth Callback - All params:', Object.fromEntries(searchParams.entries()));
    
    if (token) {
      console.log('Token found, logging in...');
      login(token);
      setTimeout(() => navigate('/dashboard'), 1500);
    } else if (error) {
      console.error('OAuth error:', error);
      setTimeout(() => navigate('/login'), 2000);
    } else {
      // If no token and no error, wait a bit then check again or redirect
      console.warn('No token or error found, waiting...');
      const timer = setTimeout(() => {
        console.warn('No token received after 3 seconds, redirecting to login');
        navigate('/login');
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [token, error, login, navigate, searchParams]);

  if (error) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
        <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md text-center">
          <AlertCircle size={48} className="mx-auto text-red-500 mb-4" />
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Authentication Failed</h2>
          <p className="text-gray-600 mb-4">{error}</p>
          <p className="text-sm text-gray-500">Redirecting to login...</p>
        </div>
      </div>
    );
  }

  if (token) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
        <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md text-center">
          <CheckCircle size={48} className="mx-auto text-green-500 mb-4" />
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Login Successful!</h2>
          <p className="text-gray-600 mb-4">Welcome! Redirecting to dashboard...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md text-center">
        <p className="text-gray-600 mb-4">Processing authentication...</p>
        <p className="text-sm text-gray-500">
          If this takes too long, check the browser console for errors.
        </p>
        <button
          onClick={() => navigate('/login')}
          className="mt-4 text-blue-600 hover:text-blue-700 underline text-sm"
        >
          Go back to login
        </button>
      </div>
    </div>
  );
};

export default OAuthCallback;
