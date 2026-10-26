export function EmptyState({ title, description, action }) {
  return (
    <div className="state-panel empty-state">
      <h3>{title}</h3>
      {description ? <p>{description}</p> : null}
      {action ? <div className="state-action">{action}</div> : null}
    </div>
  );
}
