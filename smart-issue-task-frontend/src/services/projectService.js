import { get, post, put, del } from './api';

export const projectService = {
  getAll: () => get('/api/projects'),
  getById: (id) => get(`/api/projects/${id}`),
  create: (project) => post('/api/projects', project),
  update: (id, project) => put(`/api/projects/${id}`, project),
  remove: (id) => del(`/api/projects/${id}`),
};
