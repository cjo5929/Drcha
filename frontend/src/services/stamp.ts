import axios from 'axios';

export const stamp = {
  endpoint: {
    default: '/stamp/sign',
  },

  getStamp: async (username: string): Promise<string> => {
    const { data } = await axios.get(`${stamp.endpoint.default}/${username}`);
    return data;
  },
};
