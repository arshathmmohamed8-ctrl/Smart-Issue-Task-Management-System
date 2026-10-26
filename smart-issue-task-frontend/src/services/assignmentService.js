import { get, put, del } from './api';

export const assignmentService = {
  getAssignmentsForProject: (projectId) => get(`/api/project-tasks/projects/${projectId}`),
  getAssignmentForTask: (taskId) => get(`/api/project-tasks/tasks/${taskId}`),
  assignTask: (projectId, taskId) => put(`/api/project-tasks/projects/${projectId}/tasks/${taskId}`),
  unassignTask: (projectId, taskId) => del(`/api/project-tasks/projects/${projectId}/tasks/${taskId}`),
};
