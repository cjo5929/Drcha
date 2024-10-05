import { MyInfo } from '@/types/MyInfo';

import { API } from './api';

export const member = {
  endpoint: {
    default: '/member',
    myInfo: '/member/info',
  },

  getMemberInfo: async (): Promise<MyInfo> => {
    const { data } = await API.get(`${member.endpoint.myInfo}`);

    return data;
  },
};
