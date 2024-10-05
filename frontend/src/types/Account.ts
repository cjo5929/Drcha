export type VerificationCode = {
  accountNo: string;
  code: string;
};

export type VerificationResult = {
  email: string;
  result: boolean;
};
