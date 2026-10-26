import { Menu } from 'lucide-react';
import { useLocation } from 'react-router-dom';

const pageMeta = {
  '/': { title: 'Dashboard', subtitle: 'Project overview and performance snapshot' },
  '/projects': { title: 'Projects', subtitle: 'Review projects, ownership, and summary' },
  '/tasks': { title: 'Tasks', subtitle: 'Track execution, deadlines, and priorities' },
  '/issues': { title: 'Issues', subtitle: 'Monitor reported issues and resolution flow' },
  '/assignments': { title: 'Assignments', subtitle: 'Manage project-to-task relationships' },
};

export function Header({ onToggleSidebar }) {
  const location = useLocation();
  const meta = pageMeta[location.pathname] || { title: 'Overview', subtitle: 'Smart issue and task workspace' };

  return (
    <header className="topbar">
      <div className="topbar-left">
        <button className="menu-button" type="button" aria-label="Toggle navigation" onClick={onToggleSidebar}>
          <Menu size={18} />
        </button>
        <div>
          <h1>{meta.title}</h1>
          <p>{meta.subtitle}</p>
        </div>
      </div>
    </header>
  );
}
