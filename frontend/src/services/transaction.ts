import { API } from './api';

export const transaction = {
  endpoint: {
    default: '/transaction',
    history: '/transaction/history/deposit',
  },

  getDetailHistories: async (iouId: string) => {
    const { data } = await API.get(`${transaction.endpoint.history}/${iouId}`);
    return data;
  },
};
