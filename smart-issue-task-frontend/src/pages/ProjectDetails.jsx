import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { assignmentService } from '../services/assignmentService';
import { projectService } from '../services/projectService';
import { ErrorMessage } from '../components/common/ErrorMessage';
import { LoadingState } from '../components/common/LoadingState';
import { StatusBadge } from '../components/common/StatusBadge';
import { PriorityBadge } from '../components/common/PriorityBadge';

export function ProjectDetails() {
  const { id } = useParams();
  const [project, setProject] = useState(null);
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let isMounted = true;

    async function loadProject() {
      try {
        setLoading(true);
        setError('');
        const [projectData, assignmentData] = await Promise.all([
          projectService.getById(id),
          assignmentService.getAssignmentsForProject(id),
        ]);

        if (!isMounted) return;
        setProject(projectData);
        setAssignments(assignmentData);
      } catch (loadError) {
        if (!isMounted) return;
        setError(loadError.message || 'Unable to load project details.');
      } finally {
        if (isMounted) setLoading(false);
      }
    }

    loadProject();

    return () => {
      isMounted = false;
    };
  }, [id]);

  if (loading) return <LoadingState label="Loading project details..." />;

  return (
    <div className="page-stack">
      <ErrorMessage message={error} />

      {project ? (
        <>
          <div className="panel hero-panel">
            <div>
              <div className="eyebrow">Project</div>
              <h2>{project.name}</h2>
              <p>{project.description || 'No project description provided.'}</p>
            </div>
            <div className="project-meta">
              <span>ID: #{project.id}</span>
              <span>Updated: {project.updatedAt ? new Date(project.updatedAt).toLocaleDateString() : '—'}</span>
            </div>
          </div>

          <div className="panel">
            <div className="panel-header">
              <h3>Assigned tasks</h3>
            </div>

            {assignments.length ? (
              <div className="list-rows">
                {assignments.map((assignment) => (
                  <div key={assignment.id?.taskId || assignment.task?.id} className="row-item stacked-row">
                    <div>
                      <strong>{assignment.task?.title || 'Task'}</strong>
                      <small>{assignment.task?.description || 'No description provided.'}</small>
                    </div>
                    <div className="badge-cluster">
                      <StatusBadge value={assignment.task?.status} />
                      <PriorityBadge value={assignment.task?.priority} />
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p className="muted-text">No tasks are assigned to this project yet.</p>
            )}
          </div>
        </>
      ) : (
        <p className="muted-text">No project was found.</p>
      )}

      <Link className="link-button" to="/projects">Back to projects</Link>
    </div>
  );
}
