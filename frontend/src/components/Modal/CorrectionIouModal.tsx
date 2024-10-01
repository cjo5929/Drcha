import { useEffect, useRef } from 'react';

import { ModalProps } from '@/types/ModalProps';

import styles from './Modal.module.scss';

export function CorrectionIouModal({ isOpen, onClose }: ModalProps) {
  const modalRef = useRef<HTMLDivElement>(null);

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
        <h2 className={styles.modalTitle}>차용증 수정</h2>
        <form className={styles.iouForm}>
          <div className={styles.iouDetails}>
            <label>
              차용자:
              <input
                type="text"
                name="borrower"
                // value={iouDetails.borrower}
                // onChange={handleChange}
              />
            </label>
            <label>
              대여자:
              <input
                type="text"
                name="lender"
                // value={iouDetails.lender}
                // onChange={handleChange}
              />
            </label>
            <label>
              차용일자:
              <input
                type="date"
                name="borrowDate"
                // value={iouDetails.borrowDate}
                // onChange={handleChange}
              />
            </label>
            <label>
              변제일자:
              <input
                type="date"
                name="repaymentDate"
                // value={iouDetails.repaymentDate}
                // onChange={handleChange}
              />
            </label>
            <label>
              원금:
              <input
                type="number"
                name="principal"
                // value={iouDetails.principal}
                // onChange={handleChange}
              />
            </label>
            <label>
              연이자율:
              <input
                type="number"
                name="interestRate"
                // value={iouDetails.interestRate}
                // onChange={handleChange}
                step="0.1"
              />
            </label>
          </div>
          <div className={styles.buttonContainer}>
            <button type="submit" className={styles.editButton}>
              수정 요청 보내기
            </button>
            <button
              type="button"
              className={styles.cancelButton}
              onClick={onClose}
            >
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
