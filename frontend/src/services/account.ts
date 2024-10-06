import { Account, VerificationCode, VerificationResult } from '@/types/Account';

import { API } from './api';

export const account = {
  endpoint: {
    default: '/account',
    verificationCode: '/account/verification-code',
    detail: '/account/detail',
  },

  sendVerificationCode: async (): Promise<VerificationCode> => {
    const { data } = await API.get(`${account.endpoint.verificationCode}`);

    return data;
  },

  checkVerificationCode: async (code: string): Promise<VerificationResult> => {
    const { data } = await API.post(
      `${account.endpoint.verificationCode}?code=${code}`,
    );

    return data;
  },

  getAccountDetail: async (): Promise<Account> => {
    const { data } = await API.get(`${account.endpoint.detail}`);

    return data;
  },
};
