import { ChatRoom, ChatRoomSummary } from '@/types/Chat';

import { API } from './api';

export const chat = {
  endpoint: {
    default: '/chat',
    borrowed: '/chat/borrowed',
    lent: '/chat/lent',
  },

  getBorrowedChattings: async (): Promise<ChatRoom[]> => {
    const { data } = await API.get(`${chat.endpoint.borrowed}`);

    return data;
  },

  getLentChattings: async (): Promise<ChatRoom[]> => {
    const { data } = await API.get(`${chat.endpoint.lent}`);

    return data;
  },

  getChatRoomData: async (chatRoomId: string): Promise<ChatRoomSummary> => {
    const { data } = await API.get(
      `${chat.endpoint.default}/${chatRoomId}/enter`,
    );

    return data;
  },
};
