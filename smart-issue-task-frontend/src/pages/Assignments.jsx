import { useCallback, useEffect, useMemo, useState } from 'react';
import { RefreshCcw, Trash2 } from 'lucide-react';
import { Button } from '../components/common/Button';
import { EmptyState } from '../components/common/EmptyState';
import { ErrorMessage } from '../components/common/ErrorMessage';
import { LoadingState } from '../components/common/LoadingState';
import { assignmentService } from '../services/assignmentService';
import { projectService } from '../services/projectService';
import { taskService } from '../services/taskService';

export function Assignments() {
  const [projects, setProjects] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [selectedProjectId, setSelectedProjectId] = useState('');
  const [assignments, setAssignments] = useState([]);
  const [selectedTaskId, setSelectedTaskId] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const loadAssignments = useCallback(async (projectId) => {
    if (!projectId) return [];

    const response = await assignmentService.getAssignmentsForProject(projectId);
    setAssignments(response);
    setError('');
    return response;
  }, []);

  useEffect(() => {
    let isMounted = true;

    async function loadData() {
      try {
        const [projectData, taskData] = await Promise.all([
          projectService.getAll(),
          taskService.getAll(),
        ]);
        if (!isMounted) return;
        setProjects(projectData);
        setTasks(taskData);
        setSelectedProjectId(String(projectData[0]?.id || ''));
      } catch (loadError) {
        if (!isMounted) return;
        setError(loadError.message || 'Unable to load assignments.');
      } finally {
        if (isMounted) setLoading(false);
      }
    }

    loadData();

    return () => {
      isMounted = false;
    };
  }, []);

  useEffect(() => {
    if (!selectedProjectId) return undefined;

    let isMounted = true;
    assignmentService.getAssignmentsForProject(selectedProjectId)
      .then((response) => {
        if (isMounted) {
          setAssignments(response);
          setError('');
        }
      })
      .catch((loadError) => {
        if (isMounted) setError(loadError.message || 'Unable to load assignments.');
      });

    return () => {
      isMounted = false;
    };
  }, [selectedProjectId]);

  const availableTasks = useMemo(
    () => tasks.filter((task) => !assignments.some((assignment) => String(assignment.task?.id || assignment.id?.taskId) === String(task.id))),
    [assignments, tasks]
  );

  const handleAssign = async () => {
    if (!selectedProjectId || !selectedTaskId) return;

    try {
      setSaving(true);
      setError('');
      await assignmentService.assignTask(Number(selectedProjectId), Number(selectedTaskId));
      await loadAssignments(selectedProjectId);
      setSelectedTaskId('');
    } catch (assignError) {
      setError(assignError.message || 'Unable to assign task.');
    } finally {
      setSaving(false);
    }
  };

  const handleRemove = async (taskId) => {
    try {
      setSaving(true);
      setError('');
      await assignmentService.unassignTask(Number(selectedProjectId), Number(taskId));
      await loadAssignments(selectedProjectId);
    } catch (removeError) {
      setError(removeError.message || 'Unable to remove assignment.');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <LoadingState label="Loading assignments..." />;

  return (
    <div className="page-stack">
      <ErrorMessage message={error} />

      <div className="panel assignment-panel">
        <div className="assignment-controls">
          <label>
            <span>Project</span>
            <select value={selectedProjectId} onChange={(event) => {
              setSelectedTaskId('');
              setSelectedProjectId(event.target.value);
            }}>
              {projects.map((project) => (
                <option key={project.id} value={String(project.id)}>{project.name}</option>
              ))}
            </select>
          </label>

          <label>
            <span>Task to assign</span>
            <select value={selectedTaskId} onChange={(event) => setSelectedTaskId(event.target.value)}>
              <option value="">Select a task</option>
              {availableTasks.map((task) => (
                <option key={task.id} value={String(task.id)}>{task.title}</option>
              ))}
            </select>
          </label>

          <Button onClick={handleAssign} disabled={!selectedTaskId || saving}>
            <RefreshCcw size={16} />
            <span>{saving ? 'Updating...' : 'Assign task'}</span>
          </Button>
        </div>
      </div>

      {assignments.length ? (
        <div className="table-panel">
          <table>
            <thead>
              <tr>
                <th>Project</th>
                <th>Task</th>
                <th>Assigned</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {assignments.map((assignment) => {
                const task = assignment.task;
                const taskId = task?.id || assignment.id?.taskId;
                const projectName = assignment.project?.name || projects.find((project) => String(project.id) === String(selectedProjectId))?.name || 'Project';

                return (
                  <tr key={`${selectedProjectId}-${taskId}`}>
                    <td>{projectName}</td>
                    <td>{task?.title || `Task #${taskId}`}</td>
                    <td>{assignment.assignedAt ? new Date(assignment.assignedAt).toLocaleString() : '—'}</td>
                    <td className="row-actions">
                      <button type="button" className="icon-button danger" aria-label={`Remove assignment for task ${taskId}`} onClick={() => handleRemove(taskId)}>
                        <Trash2 size={16} />
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      ) : (
        <EmptyState title="No assignments for this project" description="Select a task and assign it to the current project." />
      )}
    </div>
  );
}
