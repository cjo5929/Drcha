export type ChatMessage = {
  id: string;
  chatRoomId: string;
  content: string;
  senderId: number;
  messageType: 'SYSTEM' | 'ENTER' | 'TALK' | 'QUIT';
  createdAt: string;
  iouInfo: {
    iouId: number;
    creditorName: string;
    debtorName: string;
    iouAmount: number;
    contractStartDate: string;
    contractEndDate: string;
    interestRate: number;
    borrowerAgreement: boolean;
    lenderAgreement: boolean;
    totalAmount: number;
  };
};

export type SendChatMessage = {
  chatRoomId: string;
  senderId: number;
  content: string;
  messageType: string;
};
