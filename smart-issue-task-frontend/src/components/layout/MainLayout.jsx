import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { Header } from './Header';

export function MainLayout() {
  const [isSidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="app-shell">
      <div className={`sidebar-backdrop ${isSidebarOpen ? 'visible' : ''}`} onClick={() => setSidebarOpen(false)} />
      <div className={`sidebar-wrapper ${isSidebarOpen ? 'open' : ''}`}>
        <Sidebar onNavigate={() => setSidebarOpen(false)} />
      </div>

      <main className="main-panel">
        <Header onToggleSidebar={() => setSidebarOpen((current) => !current)} />
        <div className="content-panel">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
