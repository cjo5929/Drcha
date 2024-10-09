import axios from 'axios';

export const stamp = {
  endpoint: {
    default: '/stamp',
  },

  getStamp: async (username: string) => {
    const { data } = await axios.get(
      `${stamp.endpoint.default}/sign/${username}`,
    );
    return data;
  },
};
