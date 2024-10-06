export type Account = {
  bankName: string;
  accountNumber: string;
  accountHolderName: string;
  balance: number;
  primary: boolean;
};

export type VerificationCode = {
  accountNo: string;
  code: string;
};

export type VerificationResult = {
  email: string;
  result: boolean;
};
