import { IouDatainChatroom } from './iou';

export type MenuProps = {
  isOpen: boolean;
  onClose: () => void;
  onOpenModal: (modalType: 'create' | 'check' | 'correction') => void;
  curIou: IouDatainChatroom[];
  memberRole: string | undefined;
};
