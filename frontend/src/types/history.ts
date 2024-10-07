export type TransactionDetailHistory = {
  transactionId: number;
  amount: number;
  balanceBeforeTransaction: number;
  balanceAfterTransaction: number;
  transactionDate: string;
  transactionUniqueNo: number;
  creditorName: string;
  debtorName: string;
  description: string;
  transactionType: string;
};

export type TransactionHistory = {
  iouId: string;
  opponentName: string;
  principalAmount: number;
  contractStartDate: string;
  agreementStatus: boolean;
  contractStatus: 'DRAFTING' | 'ACTIVE' | 'COMPLETED' | 'OVERDUE' | 'CANCELLED';
};
