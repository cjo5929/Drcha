import { useEffect, useRef } from 'react';

import DOCTOR_IMAGE from '@/assets/images/dr,chayongV1.png';
import { CreateModalProps } from '@/types/ModalProps';

import styles from './Modal.module.scss';
import { Button } from '../Button/Button';

export function CreateIouModal({
  isOpen,
  onClose,
  createIou,
}: CreateModalProps) {
  const modalRef = useRef<HTMLDivElement>(null);

  const handleCreate = () => {
    createIou();
    onClose();
  };

  useEffect(() => {
    const handleOutsideClick = (event: MouseEvent) => {
      if (
        modalRef.current &&
        !modalRef.current.contains(event.target as Node)
      ) {
        onClose();
      }
    };

    if (isOpen) {
      document.addEventListener('mousedown', handleOutsideClick);
    }

    return () => {
      document.removeEventListener('mousedown', handleOutsideClick);
    };
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent} ref={modalRef}>
        <div className={styles.doctorprofile}>
          <img
            src={DOCTOR_IMAGE}
            alt="doctor"
            className={styles.doctorprofileimg}
          />
        </div>
        <div className={styles.modalTitle}>차용박사가 차용증을 작성합니다.</div>
        <div className={styles.modalBtns}>
          <Button color="lightblue" size="small" onClick={handleCreate}>
            네
          </Button>
          <Button color="lightred" size="small" onClick={onClose}>
            아니오
          </Button>
        </div>
      </div>
    </div>
  );
}
