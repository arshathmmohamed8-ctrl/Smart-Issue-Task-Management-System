const getDefaultApiBaseUrl = () => {
  if (typeof window !== 'undefined') {
    const host = window.location.hostname;
    if (host === 'localhost' || host === '127.0.0.1') {
      return 'http://localhost:8081';
    }
  }

  return 'https://smart-issue-task-api.onrender.com';
};

export const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || getDefaultApiBaseUrl()).trim().replace(/\/$/, '');

async function request(path, options = {}) {
  const headers = new Headers(options.headers || {});
  const finalPath = path.startsWith('/') ? path : `/${path}`;
  const isJsonBody = options.body && !(options.body instanceof FormData) && !headers.has('Content-Type');

  if (isJsonBody) {
    headers.set('Content-Type', 'application/json');
  }

  const requestConfig = {
    ...options,
    method: (options.method || 'GET').toUpperCase(),
    headers,
    credentials: 'include',
  };

  const response = await fetch(`${API_BASE_URL}${finalPath}`, requestConfig);

  const contentType = response.headers.get('content-type') || '';
  const hasJsonBody = contentType.includes('application/json');

  let payload = null;
  if (response.status !== 204 && response.headers.get('content-length') !== '0') {
    payload = hasJsonBody ? await response.json() : await response.text();
  }

  if (!response.ok) {
    let message = 'Request failed.';
    if (payload) {
      if (typeof payload === 'string') {
        message = payload;
      } else if (payload.message) {
        message = payload.message;
      } else if (payload.error) {
        message = payload.error;
      } else {
        message = JSON.stringify(payload);
      }
    }

    console.error('API request failed:', finalPath, response.status, message);
    const error = new Error(message);
    error.status = response.status;
    throw error;
  }

  return payload;
}

export function get(path, options = {}) {
  return request(path, { ...options, method: 'GET' });
}

export function post(path, data) {
  return request(path, {
    method: 'POST',
    body: data ? JSON.stringify(data) : null,
  });
}

export function put(path, data) {
  return request(path, {
    method: 'PUT',
    body: data ? JSON.stringify(data) : null,
  });
}

export function del(path) {
  return request(path, { method: 'DELETE' });
}

export default request;
