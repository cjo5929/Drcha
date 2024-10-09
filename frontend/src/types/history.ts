export type Transaction = {
  amount: number;
  balanceAfterTransaction: number;
  balanceBeforeTransaction: number;
  description: string;
  transactionDate: string;
  transactionId: number;
  transactionType: string;
  transactionUniqueNo: number;
};

export type TransactionDetailHistory = {
  creditorName: string;
  debtorName: string;
  repaymentDate: string;
  transactionStartDate: string;
  transactionEndDate: string;
  transactions: Transaction[];
};

export type TransactionHistory = {
  iouId: string;
  opponentName: string;
  principalAmount: number;
  contractStartDate: string;
  agreementStatus: boolean;
  contractStatus: 'DRAFTING' | 'ACTIVE' | 'COMPLETED' | 'OVERDUE' | 'CANCELLED';
};
