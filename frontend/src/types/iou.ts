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

export type IouDetailData = {
  iouId: string;
  creditorName: string;
  debtorName: string;
  iouAmount: number;
  contractStartDate: string;
  contractEndDate: string;
  interestRate: number;
  borrowerAgreement: true;
  lenderAgreement: true;
  contractStatus: 'DRAFTING' | 'ACTIVE' | 'COMPLETED' | 'OVERDUE' | 'CANCELLED';
  totalAmount: number;
  notificationSchedule: number;
  phoneNumber: string;
  daysUntilDue: number;
  iouBalance: number;
};
