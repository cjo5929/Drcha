export const URL = {
  API: import.meta.env.VITE_API_URL,
  API_LOCAL: import.meta.env.VITE_API_URL_LOCAL,
  LOGIN:
    import.meta.env.MODE === 'development'
      ? import.meta.env.VITE_API_URL_LOCAL
      : import.meta.env.VITE_API_URL_LOGIN,
};
