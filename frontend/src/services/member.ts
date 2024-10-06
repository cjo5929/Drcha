import { Info, PhoneNumber } from '@/types/Member';

import { API } from './api';

export const member = {
  endpoint: {
    default: '/member',
    myInfo: '/member/info',
    phoneNumber: '/member/phone-number',
    authStatus: '/member/auth/status',
  },

  getMemberInfo: async (): Promise<Info> => {
    const { data } = await API.get(`${member.endpoint.myInfo}`);

    return data;
  },

  registPhoneNumber: async (phoneNumber: PhoneNumber): Promise<void> => {
    const { data } = await API.post(`${member.endpoint.phoneNumber}`, {
      ...phoneNumber,
    });

    return data;
  },

  getMemberAuthStatus: async (): Promise<boolean> => {
    const { data } = await API.get(`${member.endpoint.authStatus}`);

    return data;
  },
};
