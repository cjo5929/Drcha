import { useEffect, useRef, useState } from 'react';

import { ManualModalProps } from '@/types/ModalProps';

import styles from './Modal.module.scss';
import { Button } from '../Button/Button';

interface IouFormData {
  contractEndDate: string;
  iouAmount: number;
  interestRate: number;
}

export function CorrectionIouModal({
  isOpen,
  onClose,
  createManualIou,
}: ManualModalProps) {
  const modalRef = useRef<HTMLDivElement>(null);
  const [formData, setFormData] = useState<IouFormData>({
    contractEndDate: '',
    iouAmount: 0,
    interestRate: 0,
  });

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

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'contractEndDate' ? value : Number(value),
    }));
  };

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    return date.toISOString();
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    await createManualIou(
      formData.iouAmount,
      formData.interestRate,
      formatDate(formData.contractEndDate),
    );
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent} ref={modalRef}>
        <h2 className={styles.modalTitle}>차용증 작성</h2>
        <form className={styles.iouForm} onSubmit={handleSubmit}>
          <div className={styles.iouDetails}>
            <label>
              변제일자:
              <input
                type="date"
                name="contractEndDate"
                value={formData.contractEndDate}
                onChange={handleChange}
                required
              />
            </label>
            <label>
              원금:
              <input
                type="number"
                name="iouAmount"
                value={formData.iouAmount}
                onChange={handleChange}
                required
                min="0"
              />
            </label>
            <label>
              연이자율:
              <input
                type="number"
                name="interestRate"
                value={formData.interestRate}
                onChange={handleChange}
                required
                min="0"
                step="0.1"
              />
            </label>
          </div>
          <div className={styles.buttonContainer}>
            <Button color="lightblue" size="small" type="submit">
              작성
            </Button>
            <Button
              color="lightred"
              size="small"
              onClick={onClose}
              type="button"
            >
              취소
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
