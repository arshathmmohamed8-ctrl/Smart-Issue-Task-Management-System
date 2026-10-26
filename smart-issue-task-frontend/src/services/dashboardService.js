import { get } from './api';

export const dashboardService = {
  getStatistics: () => get('/api/dashboard/statistics'),
};
