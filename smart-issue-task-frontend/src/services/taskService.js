import { get, post, put, del } from './api';

function buildTaskQuery(params = {}) {
  const searchParams = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== null && value !== undefined && value !== '') {
      searchParams.append(key, value);
    }
  });

  const queryString = searchParams.toString();
  return queryString ? `?${queryString}` : '';
}

export const taskService = {
  getAll: (status, priority) => get(`/api/tasks${buildTaskQuery({ status, priority })}`),
  getById: (id) => get(`/api/tasks/${id}`),
  create: (task) => post('/api/tasks', task),
  update: (id, task) => put(`/api/tasks/${id}`, task),
  remove: (id) => del(`/api/tasks/${id}`),
};
