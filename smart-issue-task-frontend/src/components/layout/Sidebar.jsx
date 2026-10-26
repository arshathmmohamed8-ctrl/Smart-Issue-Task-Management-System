import { BarChart3, FolderKanban, ClipboardList, AlertCircle, Link2, LayoutDashboard } from 'lucide-react';
import { NavLink } from 'react-router-dom';

const navItems = [
  { to: '/', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/projects', label: 'Projects', icon: FolderKanban },
  { to: '/tasks', label: 'Tasks', icon: ClipboardList },
  { to: '/issues', label: 'Issues', icon: AlertCircle },
  { to: '/assignments', label: 'Assignments', icon: Link2 },
];

export function Sidebar({ onNavigate }) {
  return (
    <aside className="sidebar">
      <div className="brand-wrap">
        <div className="brand-mark">S</div>
        <div>
          <div className="brand-name">Smart Issue &amp; Task</div>
          <div className="brand-subtitle">Management</div>
        </div>
      </div>

      <nav className="sidebar-nav" aria-label="Main navigation">
        {navItems.map(({ to, label, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            end={to === '/'}
            className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
            onClick={onNavigate}
          >
            <Icon size={18} />
            <span>{label}</span>
          </NavLink>
        ))}
      </nav>

      <div className="sidebar-footer">
        <div className="sidebar-footer-card">
          <BarChart3 size={16} />
          <span>Issue analytics</span>
        </div>
      </div>
    </aside>
  );
}
