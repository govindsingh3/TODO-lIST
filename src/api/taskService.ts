import axiosClient from './axiosClient';

export type Priority = 'URGENT' | 'NORMAL' | 'LOW';
export type Status = 'NOT_STARTED' | 'IN_PROGRESS' | 'DONE' | 'MISSED_DEADLINE';

export interface Task {
  id: number;
  taskDescription: string;
  priority: Priority;
  status: Status;
  deadline: string;
  createdAt: string;
  userId: number;
}

export interface CreateTaskRequest {
  taskDescription: string;
  priority: Priority;
  status: Status;
  deadline: string;
}

export type UpdateTaskRequest = CreateTaskRequest;

export interface TaskFilterRequest {
  priority?: Priority;
  status?: Status;
  deadlineFrom?: string;
  deadlineTo?: string;
  sortBy?: 'priority' | 'status' | 'deadline';
  sortOrder?: 'asc' | 'desc';
}

export const taskService = {
  getAllTasks: (filter?: TaskFilterRequest) =>
    axiosClient.get<Task[]>('/tasks', { params: filter }),

  getTaskById: (id: number) =>
    axiosClient.get<Task>(`/tasks/${id}`),

  createTask: (data: CreateTaskRequest) =>
    axiosClient.post<Task>('/tasks', data),

  updateTask: (id: number, data: UpdateTaskRequest) =>
    axiosClient.put<Task>(`/tasks/${id}`, data),

  deleteTask: (id: number) =>
    axiosClient.delete(`/tasks/${id}`),
};
