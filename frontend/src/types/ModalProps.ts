import { IouData } from './iou';

export type CheckModalProps = {
  isOpen: boolean;
  onClose: () => void;
  activeIou: IouData | null;
  memberRole: string | undefined;
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
