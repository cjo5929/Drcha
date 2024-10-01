export type MenuProps = {
  isOpen: boolean;
  onClose: () => void;
  onOpenModal: (modalType: 'create' | 'check' | 'correction') => void;
};
