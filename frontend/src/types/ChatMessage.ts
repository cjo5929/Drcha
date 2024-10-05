export type ChatMessage = {
  chatRoomId: string;
  senderId: number;
  content: string;
  messageType: 'SYSTEM' | 'ENTER' | 'TALK' | 'QUIT';
};
