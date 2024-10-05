export type ChatMessage = {
  chatRoomId: string;
  senderId: string;
  content: string;
  messageType: 'SYSTEM' | 'ENTER' | 'TALK' | 'QUIT';
};
