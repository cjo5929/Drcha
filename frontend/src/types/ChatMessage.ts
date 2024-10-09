import { IouData } from '@/types/iou';

export type ChatMessage = {
  id: string;
  chatRoomId: string;
  content: string;
  senderId: number;
  messageType: 'SYSTEM' | 'ENTER' | 'TALK' | 'QUIT' | 'IOU';
  createdAt: string;
  iouInfo: IouData;
};

export type SendChatMessage = {
  chatRoomId: string;
  senderId: number;
  content: string;
  messageType: string;
};
