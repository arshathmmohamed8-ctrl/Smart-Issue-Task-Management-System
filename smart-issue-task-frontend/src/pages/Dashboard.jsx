import { useEffect, useState } from 'react';
import { AlertCircle, FolderKanban, ListTodo, Target } from 'lucide-react';
import { dashboardService } from '../services/dashboardService';
import { projectService } from '../services/projectService';
import { taskService } from '../services/taskService';
import { issueService } from '../services/issueService';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorMessage } from '../components/common/ErrorMessage';
import { StatusBadge } from '../components/common/StatusBadge';
import { PriorityBadge } from '../components/common/PriorityBadge';

const metricConfig = [
  { key: 'projectCount', label: 'Projects', icon: FolderKanban, accent: 'teal' },
  { key: 'taskCount', label: 'Tasks', icon: ListTodo, accent: 'blue' },
  { key: 'issueCount', label: 'Issues', icon: AlertCircle, accent: 'amber' },
  { key: 'pendingTaskCount', label: 'Pending', icon: Target, accent: 'purple' },
];

export function Dashboard() {
  const [stats, setStats] = useState(null);
  const [projects, setProjects] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [issues, setIssues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let isMounted = true;

    async function loadDashboard() {
      try {
        setLoading(true);
        setError('');

        const [statsResponse, projectsResponse, tasksResponse, issuesResponse] = await Promise.all([
          dashboardService.getStatistics(),
          projectService.getAll(),
          taskService.getAll(),
          issueService.getAll(),
        ]);

        if (!isMounted) return;

        setStats(statsResponse);
        setProjects(projectsResponse.slice(0, 5));
        setTasks(tasksResponse.slice(0, 5));
        setIssues(issuesResponse.slice(0, 5));
      } catch (loadError) {
        if (!isMounted) return;
        setError(loadError.message || 'Unable to load dashboard data.');
      } finally {
        if (isMounted) setLoading(false);
      }
    }

    loadDashboard();

    return () => {
      isMounted = false;
    };
  }, []);

  if (loading) return <LoadingState label="Loading dashboard..." />;

  return (
    <div className="page-stack">
      <ErrorMessage message={error} />

      <section className="stats-grid">
        {metricConfig.map(({ key, label, icon: Icon, accent }) => (
          <article key={key} className={`stat-card stat-${accent}`}>
            <div className="stat-icon"><Icon size={18} /></div>
            <div>
              <div className="stat-value">{stats?.[key] ?? 0}</div>
              <div className="stat-label">{label}</div>
            </div>
          </article>
        ))}
      </section>

      <section className="row-grid">
        <div className="panel">
          <div className="panel-header">
            <h3>Recent projects</h3>
          </div>
          <div className="list-rows">
            {projects.length ? (
              projects.map((project) => (
                <div key={project.id} className="row-item">
                  <div>
                    <strong>{project.name}</strong>
                    <small>{project.description || 'No description provided.'}</small>
                  </div>
                  <span className="meta-pill">#{project.id}</span>
                </div>
              ))
            ) : (
              <p className="muted-text">No projects available.</p>
            )}
          </div>
        </div>

        <div className="panel">
          <div className="panel-header">
            <h3>Recent tasks</h3>
          </div>
          <div className="list-rows">
            {tasks.length ? (
              tasks.map((task) => (
                <div key={task.id} className="row-item stacked-row">
                  <div>
                    <strong>{task.title}</strong>
                    <small>{task.description || 'No summary provided.'}</small>
                  </div>
                  <div className="badge-cluster">
                    <StatusBadge value={task.status} />
                    <PriorityBadge value={task.priority} />
                  </div>
                </div>
              ))
            ) : (
              <p className="muted-text">No tasks available.</p>
            )}
          </div>
        </div>
      </section>

      <section className="panel">
        <div className="panel-header">
          <h3>Recent issues</h3>
        </div>
        <div className="list-rows">
          {issues.length ? (
            issues.map((issue) => (
              <div key={issue.id} className="row-item stacked-row">
                <div>
                  <strong>{issue.title}</strong>
                  <small>{issue.description || 'No summary provided.'}</small>
                </div>
                <div className="badge-cluster">
                  <StatusBadge value={issue.status} />
                  <PriorityBadge value={issue.priority} />
                </div>
              </div>
            ))
          ) : (
            <p className="muted-text">No issues available.</p>
          )}
        </div>
      </section>
    </div>
  );
}
