import React from 'react';
import { Task } from '../api/taskService';
import { Trash2, Edit2, Calendar, Flag, CheckCircle2 } from 'lucide-react';

interface TaskItemProps {
  task: Task;
  onEdit: (task: Task) => void;
  onDelete: (id: number) => void;
  onMarkAsDone: (task: Task) => void;
}

const TaskItem: React.FC<TaskItemProps> = ({ task, onEdit, onDelete, onMarkAsDone }) => {
  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case 'URGENT':
        return 'bg-red-100 text-red-800 border-red-300';
      case 'NORMAL':
        return 'bg-yellow-100 text-yellow-800 border-yellow-300';
      case 'LOW':
        return 'bg-green-100 text-green-800 border-green-300';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-300';
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'NOT_STARTED':
        return 'bg-gray-50 border-l-4 border-gray-400';
      case 'IN_PROGRESS':
        return 'bg-blue-50 border-l-4 border-blue-500';
      case 'DONE':
        return 'bg-green-50 border-l-4 border-green-500';
      case 'MISSED_DEADLINE':
        return 'bg-red-50 border-l-4 border-red-500';
      default:
        return 'bg-white border-l-4 border-gray-300';
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  };

  return (
    <div className={`card flex flex-col justify-between h-48 w-full ${getStatusColor(task.status)}`}>
      <div className="flex-1">
        <h3 className="text-base font-semibold text-gray-800 mb-2 line-clamp-3">{task.taskDescription}</h3>

        <div className="flex flex-wrap gap-1 mb-3">
          <span className={`px-2 py-0.5 rounded-full text-xs font-medium border ${getPriorityColor(task.priority)}`}>
            <Flag size={12} className="inline mr-0.5" />
            {task.priority}
          </span>
          <span className="px-2 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-700 border border-gray-300">
            {task.status.replace(/_/g, ' ')}
          </span>
        </div>

        <div className="flex items-center gap-2 text-xs text-gray-600">
          <Calendar size={14} />
          <span>{formatDate(task.deadline)}</span>
        </div>
      </div>

      <div className="flex gap-1 pt-3 border-t border-gray-200">
        {task.status !== 'DONE' && (
          <button
            onClick={() => onMarkAsDone(task)}
            className="p-1 text-green-600 hover:bg-green-100 rounded transition-colors"
            title="Mark as done"
          >
            <CheckCircle2 size={16} />
          </button>
        )}
        <button
          onClick={() => onEdit(task)}
          className="p-1 text-blue-600 hover:bg-blue-100 rounded transition-colors"
          title="Edit task"
        >
          <Edit2 size={16} />
        </button>
        <button
          onClick={() => onDelete(task.id)}
          className="p-1 text-red-600 hover:bg-red-100 rounded transition-colors"
          title="Delete task"
        >
          <Trash2 size={16} />
        </button>
      </div>
    </div>
  );
};

export default TaskItem;
