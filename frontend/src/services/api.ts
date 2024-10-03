import axios from 'axios';

const apiVersion = 'api/v1';

const headers = {
  'Content-Type': 'application/json',
  'Access-Control-Allow-Credentials': 'true',
  Accept: 'application/json',
};

export const API = axios.create({
  baseURL: `http://localhost:8080/${apiVersion}`,
  headers,
  withCredentials: true,
});
