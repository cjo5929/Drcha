/* eslint-disable sort-exports/sort-exports */
import axios from 'axios';

import { URL } from '@/constants/url';

export const baseURL =
  import.meta.env.MODE === 'development' ? URL.API_LOCAL : URL.API;

const apiVersion = '/api/v1';

const headers = {
  'Content-Type': 'application/json',
  'Access-Control-Allow-Credentials': 'true',
  Accept: 'application/json',
};

export const API = axios.create({
  baseURL: baseURL + apiVersion,
  headers,
  withCredentials: true,
});
