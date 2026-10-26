import { useCallback, useEffect, useMemo, useState } from 'react';
import { Pencil, Plus, Trash2 } from 'lucide-react';
import { Button } from '../components/common/Button';
import { EmptyState } from '../components/common/EmptyState';
import { ErrorMessage } from '../components/common/ErrorMessage';
import { LoadingState } from '../components/common/LoadingState';
import { Modal } from '../components/common/Modal';
import { SearchInput } from '../components/common/SearchInput';
import { projectService } from '../services/projectService';

const defaultProject = { id: '', name: '', description: '' };

export function Projects() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [editProject, setEditProject] = useState(null);
  const [formData, setFormData] = useState(defaultProject);
  const [submitting, setSubmitting] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);

  const filteredProjects = useMemo(() => {
    const query = search.trim().toLowerCase();
    if (!query) return projects;

    return projects.filter((project) =>
      project.name.toLowerCase().includes(query) ||
      (project.description || '').toLowerCase().includes(query)
    );
  }, [projects, search]);

  const loadProjects = useCallback(async () => {
    try {
      const response = await projectService.getAll();
      setProjects(response);
      setError('');
    } catch (loadError) {
      setError(loadError.message || 'Unable to load projects.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    let isMounted = true;

    projectService.getAll()
      .then((response) => {
        if (isMounted) {
          setProjects(response);
          setError('');
        }
      })
      .catch((loadError) => {
        if (isMounted) {
          setError(loadError.message || 'Unable to load projects.');
        }
      })
      .finally(() => {
        if (isMounted) setLoading(false);
      });

    return () => {
      isMounted = false;
    };
  }, []);

  const openCreateModal = () => {
    setEditProject(null);
    setFormData(defaultProject);
    setModalOpen(true);
  };

  const openEditModal = (project) => {
    setEditProject(project);
    setFormData({
      id: project.id,
      name: project.name,
      description: project.description || '',
    });
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setEditProject(null);
    setFormData(defaultProject);
  };

  const handleFieldChange = (field, value) => {
    setFormData((current) => ({ ...current, [field]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const trimmedName = formData.name.trim();

    if (!trimmedName) {
      setError('Project name is required.');
      return;
    }

    try {
      setSubmitting(true);
      setError('');

      const payload = { ...formData, name: trimmedName, description: formData.description.trim() };

      if (editProject) {
        await projectService.update(editProject.id, payload);
      } else {
        await projectService.create(payload);
      }

      closeModal();
      await loadProjects();
    } catch (submitError) {
      setError(submitError.message || 'Unable to save project.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;

    try {
      setError('');
      await projectService.remove(deleteTarget.id);
      setDeleteTarget(null);
      await loadProjects();
    } catch (deleteError) {
      setError(deleteError.message || 'Unable to delete project.');
    }
  };

  if (loading) return <LoadingState label="Loading projects..." />;

  return (
    <div className="page-stack">
      <div className="page-toolbar">
        <SearchInput value={search} onChange={setSearch} placeholder="Search projects" />
        <Button onClick={openCreateModal}>
          <Plus size={16} />
          <span>Create Project</span>
        </Button>
      </div>

      <ErrorMessage message={error} />

      {filteredProjects.length ? (
        <div className="table-panel">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Updated</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredProjects.map((project) => (
                <tr key={project.id}>
                  <td>#{project.id}</td>
                  <td>{project.name}</td>
                  <td>{project.description || '—'}</td>
                  <td>{project.updatedAt ? new Date(project.updatedAt).toLocaleDateString() : '—'}</td>
                  <td className="row-actions">
                    <button type="button" className="icon-button" aria-label={`Edit ${project.name}`} onClick={() => openEditModal(project)}>
                      <Pencil size={16} />
                    </button>
                    <button type="button" className="icon-button danger" aria-label={`Delete ${project.name}`} onClick={() => setDeleteTarget(project)}>
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
          title="No projects match your search"
          description="Create a new project or adjust the query."
          action={<Button onClick={openCreateModal}><Plus size={16} /><span>Create Project</span></Button>}
        />
      )}

      <Modal title={editProject ? 'Edit project' : 'Create project'} isOpen={isModalOpen} onClose={closeModal}>
        <form className="form-grid" onSubmit={handleSubmit}>
          <label>
            <span>Project ID</span>
            <input
              type="number"
              value={formData.id}
              onChange={(event) => handleFieldChange('id', event.target.value)}
              disabled={Boolean(editProject)}
              required
            />
          </label>

          <label>
            <span>Project name</span>
            <input
              type="text"
              value={formData.name}
              onChange={(event) => handleFieldChange('name', event.target.value)}
              required
            />
          </label>

          <label className="full-width">
            <span>Description</span>
            <textarea
              rows="4"
              value={formData.description}
              onChange={(event) => handleFieldChange('description', event.target.value)}
            />
          </label>

          <div className="form-actions full-width">
            <Button type="button" variant="secondary" onClick={closeModal} disabled={submitting}>Cancel</Button>
            <Button type="submit" disabled={submitting}>
              {submitting ? 'Saving...' : editProject ? 'Save changes' : 'Create project'}
            </Button>
          </div>
        </form>
      </Modal>

      <Modal title="Delete project" isOpen={Boolean(deleteTarget)} onClose={() => setDeleteTarget(null)}>
        <div className="confirm-panel">
          <p>Delete <strong>{deleteTarget?.name}</strong>? This action removes the project and any existing assignment rows for it.</p>
          <div className="form-actions">
            <Button type="button" variant="secondary" onClick={() => setDeleteTarget(null)}>Cancel</Button>
            <Button type="button" variant="danger" onClick={handleDelete}>Delete</Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
