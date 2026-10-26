import { Link } from 'react-router-dom';

export function NotFound() {
  return (
    <div className="page-stack centered-page">
      <div className="panel empty-state">
        <h2>Page not found</h2>
        <p>The page you requested does not exist.</p>
        <Link className="link-button" to="/">Return to dashboard</Link>
      </div>
    </div>
  );
}
