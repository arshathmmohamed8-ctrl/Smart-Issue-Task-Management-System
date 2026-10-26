import { get, post, put, del } from './api';

export const issueService = {
  getAll: () => get('/api/issues'),
  getById: (id) => get(`/api/issues/${id}`),
  create: (issue) => post('/api/issues', issue),
  update: (id, issue) => put(`/api/issues/${id}`, issue),
  remove: (id) => del(`/api/issues/${id}`),
};
