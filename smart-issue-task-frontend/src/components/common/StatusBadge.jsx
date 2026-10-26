const statusClasses = {
  PENDING: 'badge badge-warning',
  OPEN: 'badge badge-info',
  IN_PROGRESS: 'badge badge-accent',
  COMPLETED: 'badge badge-success',
  RESOLVED: 'badge badge-success',
};

export function StatusBadge({ value }) {
  if (!value) return null;
  const label = String(value).replace(/_/g, ' ');

  return <span className={statusClasses[value] || 'badge badge-default'}>{label}</span>;
}
