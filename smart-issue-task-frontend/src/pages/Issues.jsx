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
import { issueService } from '../services/issueService';

const defaultIssue = {
  id: '',
  title: '',
  description: '',
  priority: 'MEDIUM',
  status: 'OPEN',
};

export function Issues() {
  const [issues, setIssues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [priorityFilter, setPriorityFilter] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [editIssue, setEditIssue] = useState(null);
  const [formData, setFormData] = useState(defaultIssue);
  const [submitting, setSubmitting] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);

  const loadIssues = useCallback(async () => {
    try {
      const response = await issueService.getAll();
      setIssues(response);
      setError('');
    } catch (loadError) {
      setError(loadError.message || 'Unable to load issues.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    let isMounted = true;

    issueService.getAll()
      .then((response) => {
        if (isMounted) {
          setIssues(response);
          setError('');
        }
      })
      .catch((loadError) => {
        if (isMounted) {
          setError(loadError.message || 'Unable to load issues.');
        }
      })
      .finally(() => {
        if (isMounted) setLoading(false);
      });

    return () => {
      isMounted = false;
    };
  }, []);

  const filteredIssues = useMemo(() => {
    const query = search.trim().toLowerCase();
    return issues.filter((issue) => {
      const matchesSearch = !query || issue.title.toLowerCase().includes(query) || (issue.description || '').toLowerCase().includes(query);
      const matchesStatus = !statusFilter || issue.status === statusFilter;
      const matchesPriority = !priorityFilter || issue.priority === priorityFilter;
      return matchesSearch && matchesStatus && matchesPriority;
    });
  }, [issues, priorityFilter, search, statusFilter]);

  const openCreateModal = () => {
    setEditIssue(null);
    setFormData(defaultIssue);
    setModalOpen(true);
  };

  const openEditModal = (issue) => {
    setEditIssue(issue);
    setFormData({
      id: issue.id,
      title: issue.title,
      description: issue.description || '',
      priority: issue.priority,
      status: issue.status,
    });
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditIssue(null);
    setFormData(defaultIssue);
  };

  const handleFieldChange = (field, value) => {
    setFormData((current) => ({ ...current, [field]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const trimmedTitle = formData.title.trim();

    if (!trimmedTitle) {
      setError('Issue title is required.');
      return;
    }

    if (!formData.priority || !formData.status) {
      setError('Issue priority and status are required.');
      return;
    }

    try {
      setSubmitting(true);
      setError('');

      const payload = {
        ...formData,
        title: trimmedTitle,
        description: formData.description.trim(),
      };

      if (editIssue) {
        await issueService.update(editIssue.id, payload);
      } else {
        await issueService.create(payload);
      }

      closeModal();
      await loadIssues();
    } catch (submitError) {
      setError(submitError.message || 'Unable to save issue.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;

    try {
      setError('');
      await issueService.remove(deleteTarget.id);
      setDeleteTarget(null);
      await loadIssues();
    } catch (deleteError) {
      setError(deleteError.message || 'Unable to delete issue.');
    }
  };

  if (loading) return <LoadingState label="Loading issues..." />;

  return (
    <div className="page-stack">
      <div className="page-toolbar">
        <SearchInput value={search} onChange={setSearch} placeholder="Search issues" />
        <div className="filter-group">
          <select value={statusFilter} onChange={(event) => setStatusFilter(event.target.value)}>
            <option value="">All statuses</option>
            <option value="OPEN">OPEN</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="RESOLVED">RESOLVED</option>
          </select>
          <select value={priorityFilter} onChange={(event) => setPriorityFilter(event.target.value)}>
            <option value="">All priorities</option>
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
          </select>
        </div>
        <Button onClick={openCreateModal}>
          <Plus size={16} />
          <span>Create Issue</span>
        </Button>
      </div>

      <ErrorMessage message={error} />

      {filteredIssues.length ? (
        <div className="table-panel">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Issue</th>
                <th>Status</th>
                <th>Priority</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredIssues.map((issue) => (
                <tr key={issue.id}>
                  <td>#{issue.id}</td>
                  <td>
                    <div className="text-cell">
                      <strong>{issue.title}</strong>
                      <small>{issue.description || 'No description provided.'}</small>
                    </div>
                  </td>
                  <td><StatusBadge value={issue.status} /></td>
                  <td><PriorityBadge value={issue.priority} /></td>
                  <td>{issue.createdDate ? new Date(issue.createdDate).toLocaleDateString() : '—'}</td>
                  <td className="row-actions">
                    <button type="button" className="icon-button" aria-label={`Edit ${issue.title}`} onClick={() => openEditModal(issue)}>
                      <Pencil size={16} />
                    </button>
                    <button type="button" className="icon-button danger" aria-label={`Delete ${issue.title}`} onClick={() => setDeleteTarget(issue)}>
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
          title="No issues available"
          description="Create a report or adjust the active filters."
          action={<Button onClick={openCreateModal}><Plus size={16} /><span>Create Issue</span></Button>}
        />
      )}

      <Modal title={editIssue ? 'Edit issue' : 'Create issue'} isOpen={isModalOpen} onClose={closeModal}>
        <form className="form-grid" onSubmit={handleSubmit}>
          <label>
            <span>Issue ID</span>
            <input
              type="number"
              value={formData.id}
              onChange={(event) => handleFieldChange('id', event.target.value)}
              disabled={Boolean(editIssue)}
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
              <option value="OPEN">OPEN</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="RESOLVED">RESOLVED</option>
            </select>
          </label>

          <label className="full-width">
            <span>Description</span>
            <textarea rows="4" value={formData.description} onChange={(event) => handleFieldChange('description', event.target.value)} />
          </label>

          <div className="form-actions full-width">
            <Button type="button" variant="secondary" onClick={closeModal} disabled={submitting}>Cancel</Button>
            <Button type="submit" disabled={submitting}>{submitting ? 'Saving...' : editIssue ? 'Save changes' : 'Create issue'}</Button>
          </div>
        </form>
      </Modal>

      <Modal title="Delete issue" isOpen={Boolean(deleteTarget)} onClose={() => setDeleteTarget(null)}>
        <div className="confirm-panel">
          <p>Delete <strong>{deleteTarget?.title}</strong>? This removes the issue from the workspace.</p>
          <div className="form-actions">
            <Button type="button" variant="secondary" onClick={() => setDeleteTarget(null)}>Cancel</Button>
            <Button type="button" variant="danger" onClick={handleDelete}>Delete</Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
