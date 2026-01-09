import React, { useState, useEffect } from 'react';
import { Task, Priority, Status, CreateTaskRequest, UpdateTaskRequest } from '../api/taskService';
import { AlertCircle } from 'lucide-react';

interface TaskFormProps {
  task?: Task;
  onSubmit: (data: CreateTaskRequest | UpdateTaskRequest) => Promise<void>;
  onCancel: () => void;
}

const TaskForm: React.FC<TaskFormProps> = ({ task, onSubmit, onCancel }) => {
  const [description, setDescription] = useState('');
  const [priority, setPriority] = useState<Priority>('NORMAL');
  const [status, setStatus] = useState<Status>('NOT_STARTED');
  const [deadline, setDeadline] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (task) {
      setDescription(task.taskDescription);
      setPriority(task.priority);
      setStatus(task.status);
      // Extract date part from ISO datetime string
      const datePart = task.deadline.substring(0, 10);
      setDeadline(datePart);
    }
  }, [task]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!description.trim()) {
      setError('Task description is required');
      return;
    }

    if (!deadline) {
      setError('Deadline is required');
      return;
    }

    setLoading(true);
    try {
      await onSubmit({
        taskDescription: description,
        priority,
        status,
        deadline: `${deadline}T00:00:00`,
      });
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to save task');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow-md p-6 mb-6">
      <h3 className="text-lg font-semibold text-gray-800 mb-4">
        {task ? 'Edit Task' : 'Add New Task'}
      </h3>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg flex items-center gap-2 text-red-700">
          <AlertCircle size={18} />
          {error}
        </div>
      )}

      <div className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Enter task description"
            rows={3}
          />
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Priority</label>
            <select
              value={priority}
              onChange={(e) => setPriority(e.target.value as Priority)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="LOW">Low</option>
              <option value="NORMAL">Normal</option>
              <option value="URGENT">Urgent</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
            <select
              value={status}
              onChange={(e) => setStatus(e.target.value as Status)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="NOT_STARTED">Not Started</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="DONE">Done</option>
              <option value="MISSED_DEADLINE">Missed Deadline</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Deadline</label>
            <input
              type="date"
              value={deadline}
              onChange={(e) => setDeadline(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        <div className="flex gap-2 justify-end">
          <button
            type="button"
            onClick={onCancel}
            className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors"
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={loading}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:bg-gray-400"
          >
            {loading ? 'Saving...' : task ? 'Update Task' : 'Create Task'}
          </button>
        </div>
      </div>
    </form>
  );
};

export default TaskForm;
