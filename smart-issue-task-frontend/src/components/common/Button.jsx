export function Button({ children, type = 'button', variant = 'primary', className = '', disabled = false, onClick, ...props }) {
  const variantClass = {
    primary: 'button button-primary',
    secondary: 'button button-secondary',
    danger: 'button button-danger',
    ghost: 'button button-ghost',
  }[variant] || 'button button-primary';

  return (
    <button
      type={type}
      className={`${variantClass} ${className}`.trim()}
      disabled={disabled}
      onClick={onClick}
      {...props}
    >
      {children}
    </button>
  );
}
