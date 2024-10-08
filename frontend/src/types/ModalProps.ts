import { IouDatainChatroom } from './iou';

export type CheckModalProps = {
  isOpen: boolean;
  onClose: () => void;
  activeIou: IouDatainChatroom | null;
};

export type CreateModalProps = {
  isOpen: boolean;
  onClose: () => void;
  createIou: () => void;
};

export type ManualModalProps = {
  isOpen: boolean;
  onClose: () => void;
  createManualIou: (
    iouAmount: number,
    interestRate: number,
    contractEndDate: string,
  ) => void;
};
