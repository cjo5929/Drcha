import { VerificationCode, VerificationResult } from '@/types/Account';

import { API } from './api';

export const account = {
  endpoint: {
    default: '/account',
    verificationCode: '/account/verification-code',
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
};
