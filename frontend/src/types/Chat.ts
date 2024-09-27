export type ChatRoom = {
  chatRoomId: number;
  name: string;
  trustStatus?: string;
  avatarUrl: string;
  contractStatus: 'DRAFTING' | 'ACTIVE' | 'COMPLETED' | 'OVERDUE' | 'CANCELLED';
  lastMessage: string;
  iouAmount: number;
  daysUntilDue: number;
  unreadCount: number;
};
