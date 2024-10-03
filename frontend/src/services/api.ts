import axios from 'axios';

const apiVersion = 'api/v1';

const headers = {
  'Content-Type': 'application/json',
  'Access-Control-Allow-Credentials': 'true',
  Accept: 'application/json',
};

export const API = axios.create({
  baseURL: `http://j11a205.p.ssafy.io/${apiVersion}`,
  headers,
  withCredentials: true,
});
