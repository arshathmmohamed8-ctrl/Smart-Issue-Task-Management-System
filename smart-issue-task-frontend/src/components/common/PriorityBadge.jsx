const priorityClasses = {
  LOW: 'badge badge-low',
  MEDIUM: 'badge badge-medium',
  HIGH: 'badge badge-high',
};

export function PriorityBadge({ value }) {
  if (!value) return null;
  return <span className={priorityClasses[value] || 'badge badge-default'}>{String(value)}</span>;
}
