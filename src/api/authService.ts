import axiosClient from './axiosClient';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  name?: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  message: string;
}

export const authService = {
  signup: (data: SignupRequest) =>
    axiosClient.post<AuthResponse>('/auth/signup', data),

  login: (data: LoginRequest) =>
    axiosClient.post<AuthResponse>('/auth/login', data),

  logout: () => {
    localStorage.removeItem('jwt_token');
  },

  getToken: () => localStorage.getItem('jwt_token'),

  isAuthenticated: () => !!localStorage.getItem('jwt_token'),
};
