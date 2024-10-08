import { useEffect, useRef } from 'react';

import { iou } from '@/services/iou';
import { CheckModalProps } from '@/types/ModalProps';

import styles from './Modal.module.scss';
import { Button } from '../Button/Button';

export function CheckIouModal({ isOpen, onClose, activeIou }: CheckModalProps) {
  const modalRef = useRef<HTMLDivElement>(null);
  const handleCheck = async () => {
    if (activeIou) {
      await iou.agreeIou(activeIou?.iouId);
    }
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

  if (!activeIou) {
    return (
      <div className={styles.modalOverlay}>
        <div className={styles.modalContent} ref={modalRef}>
          <div className={styles.modalTitle}>작성된 차용증이 없습니다.</div>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent} ref={modalRef}>
        <Button color="lightblue" size="small" onClick={handleCheck}>
          동의
        </Button>
      </div>
    </div>
  );
}
