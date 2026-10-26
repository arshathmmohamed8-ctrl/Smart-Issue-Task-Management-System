import { useCallback, useEffect, useMemo, useState } from 'react';
import { Pencil, Plus, Trash2 } from 'lucide-react';
import { Button } from '../components/common/Button';
import { EmptyState } from '../components/common/EmptyState';
import { ErrorMessage } from '../components/common/ErrorMessage';
import { LoadingState } from '../components/common/LoadingState';
import { Modal } from '../components/common/Modal';
import { PriorityBadge } from '../components/common/PriorityBadge';
import { SearchInput } from '../components/common/SearchInput';
import { StatusBadge } from '../components/common/StatusBadge';
import { assignmentService } from '../services/assignmentService';
import { projectService } from '../services/projectService';
import { taskService } from '../services/taskService';

const defaultTask = {
  id: '',
  title: '',
  description: '',
  priority: 'MEDIUM',
  status: 'PENDING',
  deadline: '',
};

export function Tasks() {
  const [tasks, setTasks] = useState([]);
  const [assignments, setAssignments] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [priorityFilter, setPriorityFilter] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [editTask, setEditTask] = useState(null);
  const [formData, setFormData] = useState(defaultTask);
  const [submitting, setSubmitting] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);

  const fetchTasks = useCallback(async () => {
    const response = await taskService.getAll(statusFilter, priorityFilter);
    const projects = await projectService.getAll();
    const projectAssignments = await Promise.all(
      projects.map((project) => assignmentService.getAssignmentsForProject(project.id))
    );
    const projectMap = {};

    projectAssignments.flat().forEach((assignment) => {
      const taskId = assignment.task?.id || assignment.id?.taskId;
      const projectName = assignment.project?.name || projects.find(
        (project) => String(project.id) === String(assignment.id?.projectId)
      )?.name;

      if (taskId && projectName) {
        projectMap[taskId] = projectName;
      }
    });

    response.forEach((task) => {
      if (!projectMap[task.id]) {
        projectMap[task.id] = 'Unassigned';
      }
    });

    return { response, projectMap };
  }, [priorityFilter, statusFilter]);

  const loadTasks = async () => {
    try {
      const { response, projectMap } = await fetchTasks();
      setTasks(response);
      setAssignments(projectMap);
      setError('');
    } catch (loadError) {
      setError(loadError.message || 'Unable to load tasks.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    let isMounted = true;

    fetchTasks()
      .then(({ response, projectMap }) => {
        if (isMounted) {
          setTasks(response);
          setAssignments(projectMap);
          setError('');
        }
      })
      .catch((loadError) => {
        if (isMounted) setError(loadError.message || 'Unable to load tasks.');
      })
      .finally(() => {
        if (isMounted) setLoading(false);
      });

    return () => {
      isMounted = false;
    };
  }, [fetchTasks]);

  const filteredTasks = useMemo(() => {
    const query = search.trim().toLowerCase();
    return tasks.filter((task) => {
      const matchesSearch = !query || task.title.toLowerCase().includes(query) || (task.description || '').toLowerCase().includes(query);
      return matchesSearch;
    });
  }, [tasks, search]);

  const openCreateModal = () => {
    setEditTask(null);
    setFormData(defaultTask);
    setModalOpen(true);
  };

  const openEditModal = (task) => {
    setEditTask(task);
    setFormData({
      id: task.id,
      title: task.title,
      description: task.description || '',
      priority: task.priority,
      status: task.status,
      deadline: task.deadline || '',
    });
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditTask(null);
    setFormData(defaultTask);
  };

  const handleFieldChange = (field, value) => {
    setFormData((current) => ({ ...current, [field]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const trimmedTitle = formData.title.trim();

    if (!trimmedTitle) {
      setError('Task title is required.');
      return;
    }

    if (!formData.priority || !formData.status) {
      setError('Task priority and status are required.');
      return;
    }

    try {
      setSubmitting(true);
      setError('');
      const payload = {
        ...formData,
        title: trimmedTitle,
        description: formData.description.trim(),
        deadline: formData.deadline || null,
      };

      if (editTask) {
        await taskService.update(editTask.id, payload);
      } else {
        await taskService.create(payload);
      }

      closeModal();
      await loadTasks();
    } catch (submitError) {
      setError(submitError.message || 'Unable to save task.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;

    try {
      setError('');
      await taskService.remove(deleteTarget.id);
      setDeleteTarget(null);
      await loadTasks();
    } catch (deleteError) {
      setError(deleteError.message || 'Unable to delete task.');
    }
  };

  if (loading) return <LoadingState label="Loading tasks..." />;

  return (
    <div className="page-stack">
      <div className="page-toolbar">
        <SearchInput value={search} onChange={setSearch} placeholder="Search tasks" />
        <div className="filter-group">
          <select value={statusFilter} onChange={(event) => {
            setError('');
            setLoading(true);
            setStatusFilter(event.target.value);
          }}>
            <option value="">All statuses</option>
            <option value="PENDING">PENDING</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="COMPLETED">COMPLETED</option>
          </select>
          <select value={priorityFilter} onChange={(event) => {
            setError('');
            setLoading(true);
            setPriorityFilter(event.target.value);
          }}>
            <option value="">All priorities</option>
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
          </select>
        </div>
        <Button onClick={openCreateModal}>
          <Plus size={16} />
          <span>Create Task</span>
        </Button>
      </div>

      <ErrorMessage message={error} />

      {filteredTasks.length ? (
        <div className="table-panel">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Task</th>
                <th>Project</th>
                <th>Status</th>
                <th>Priority</th>
                <th>Deadline</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredTasks.map((task) => (
                <tr key={task.id}>
                  <td>#{task.id}</td>
                  <td>
                    <div className="text-cell">
                      <strong>{task.title}</strong>
                      <small>{task.description || 'No description provided.'}</small>
                    </div>
                  </td>
                  <td>{assignments[task.id] || 'Unassigned'}</td>
                  <td><StatusBadge value={task.status} /></td>
                  <td><PriorityBadge value={task.priority} /></td>
                  <td>{task.deadline ? new Date(task.deadline).toLocaleDateString() : '—'}</td>
                  <td className="row-actions">
                    <button type="button" className="icon-button" aria-label={`Edit ${task.title}`} onClick={() => openEditModal(task)}>
                      <Pencil size={16} />
                    </button>
                    <button type="button" className="icon-button danger" aria-label={`Delete ${task.title}`} onClick={() => setDeleteTarget(task)}>
                      <Trash2 size={16} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <EmptyState
          title="No tasks match your filters"
          description="Add a new task or adjust the search criteria."
          action={<Button onClick={openCreateModal}><Plus size={16} /><span>Create Task</span></Button>}
        />
      )}

      <Modal title={editTask ? 'Edit task' : 'Create task'} isOpen={isModalOpen} onClose={closeModal}>
        <form className="form-grid" onSubmit={handleSubmit}>
          <label>
            <span>Task ID</span>
            <input
              type="number"
              value={formData.id}
              onChange={(event) => handleFieldChange('id', event.target.value)}
              disabled={Boolean(editTask)}
              required
            />
          </label>

          <label>
            <span>Title</span>
            <input
              type="text"
              value={formData.title}
              onChange={(event) => handleFieldChange('title', event.target.value)}
              required
            />
          </label>

          <label>
            <span>Priority</span>
            <select value={formData.priority} onChange={(event) => handleFieldChange('priority', event.target.value)}>
              <option value="LOW">LOW</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="HIGH">HIGH</option>
            </select>
          </label>

          <label>
            <span>Status</span>
            <select value={formData.status} onChange={(event) => handleFieldChange('status', event.target.value)}>
              <option value="PENDING">PENDING</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="COMPLETED">COMPLETED</option>
            </select>
          </label>

          <label className="full-width">
            <span>Description</span>
            <textarea rows="4" value={formData.description} onChange={(event) => handleFieldChange('description', event.target.value)} />
          </label>

          <label>
            <span>Deadline</span>
            <input type="date" value={formData.deadline} onChange={(event) => handleFieldChange('deadline', event.target.value)} />
          </label>

          <div className="form-actions full-width">
            <Button type="button" variant="secondary" onClick={closeModal} disabled={submitting}>Cancel</Button>
            <Button type="submit" disabled={submitting}>{submitting ? 'Saving...' : editTask ? 'Save changes' : 'Create task'}</Button>
          </div>
        </form>
      </Modal>

      <Modal title="Delete task" isOpen={Boolean(deleteTarget)} onClose={() => setDeleteTarget(null)}>
        <div className="confirm-panel">
          <p>Delete <strong>{deleteTarget?.title}</strong>? This removes the task and any project assignment linked to it.</p>
          <div className="form-actions">
            <Button type="button" variant="secondary" onClick={() => setDeleteTarget(null)}>Cancel</Button>
            <Button type="button" variant="danger" onClick={handleDelete}>Delete</Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
