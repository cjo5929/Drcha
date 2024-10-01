import { useEffect, useRef } from 'react';

import { ModalProps } from '@/types/ModalProps';

import styles from './Modal.module.scss';

// 아래 내용을 가져다쓸것
// const [isModalOpen, setIsModalOpen] = useState(false);

// <button onClick={() => setIsModalOpen(true)}>모달 열기</button>

// <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="채팅 정보">
//   <p>이곳에 채팅 관련 정보나 설정을 표시할 수 있습니다.</p>
// </Modal>

export function CheckIouModal({ isOpen, onClose }: ModalProps) {
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
        <h2 className={styles.modalTitle}>차용증 확인</h2>
        <div className={styles.iouImageContainer}>
          <img
            src="/path-to-iou-image.jpg"
            alt="차용증"
            className={styles.iouImage}
          />
        </div>
        <div className={styles.iouDetails}>
          <p>
            <strong>차용자:</strong>
          </p>
          <p>
            <strong>대여자:</strong>
          </p>
          <p>
            <strong>차용일자:</strong>
          </p>
          <p>
            <strong>변제일자:</strong>
          </p>
          <p>
            <strong>원금:</strong>
          </p>
          <p>
            <strong>연이자율:</strong>
          </p>
        </div>
        <div className={styles.buttonContainer}>
          <button
            className={styles.editButton}
            onClick={() => {
              /* 동의 요청 로직 */
            }}
          >
            동의
          </button>
          <button className={styles.cancelButton} onClick={onClose}>
            취소
          </button>
        </div>
      </div>
    </div>
  );
}
