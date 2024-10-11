import React, { useEffect, useRef } from 'react';

import styles from './Modal.module.scss';

// 아래 내용을 가져다쓸것
// const [isModalOpen, setIsModalOpen] = useState(false);

// <button onClick={() => setIsModalOpen(true)}>모달 열기</button>

// <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="채팅 정보">
//   <p>이곳에 채팅 관련 정보나 설정을 표시할 수 있습니다.</p>
// </Modal>

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  children: React.ReactNode;
  title?: string;
}

export function Modal({ isOpen, onClose, children, title }: ModalProps) {
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
        {title && <h2 className={styles.modalTitle}>{title}</h2>}
        <button className={styles.closeButton} onClick={onClose}>
          &times;
        </button>
        {children}
      </div>
    </div>
  );
}
