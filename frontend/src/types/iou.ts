export type IouData = {
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
  creditorPhoneNumber: string;
  debtorPhoneNumber: string;
};

export type IouDatainChatroom = {
  iouId: string;
  iouAmount: number;
  contractStartDate: string;
  contractEndDate: string;
  interestRate: number;
  totalAmount: number;
  contractStatus: 'DRAFTING' | 'ACTIVE' | 'COMPLETED' | 'OVERDUE' | 'CANCELLED';
  borrowerAgreement: boolean;
  lenderAgreement: boolean;
};

export type IouDetailData = {
  iouId: string;
  creditorName: string;
  debtorName: string;
  iouAmount: number;
  contractStartDate: string;
  contractEndDate: string;
  interestRate: number;
  borrowerAgreement: boolean;
  lenderAgreement: boolean;
  contractStatus: 'DRAFTING' | 'ACTIVE' | 'COMPLETED' | 'OVERDUE' | 'CANCELLED';
  totalAmount: number;
  notificationSchedule: number;
  phoneNumber: string;
  daysUntilDue: number;
  iouBalance: number;
  virtualAccountNumber: string;
};

export type ManualIouData = {
  chatRoomId: string;
  iouAmount: number;
  interestRate: number;
  contractEndDate: string;
};
