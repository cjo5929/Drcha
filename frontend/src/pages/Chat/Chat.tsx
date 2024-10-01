/* eslint-disable @typescript-eslint/indent */
import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import HamburgerSVG from '@/assets/icons/hamburger.svg?react';
import LeftarrowSVG from '@/assets/icons/leftArrow.svg?react';
import { CheckIouModal } from '@/components/Modal/CheckIouModal';
import { CorrectionIouModal } from '@/components/Modal/CorrectionIouModal';
import { CreateIouModal } from '@/components/Modal/CreateIouModal';

import styles from './Chat.module.scss';
import { ChatContent } from './ChatContent';
import { Menu } from './Menu';
import { SendButton } from './SendButton';

export function Chat() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [modalType, setModalType] = useState<
    'create' | 'correction' | 'check' | null
  >(null);
  const menuRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();

  const handleOpenModal = (type: 'create' | 'correction' | 'check') => {
    setModalType(type);
  };

  const handleCloseModal = () => {
    setModalType(null);
  };

  useEffect(() => {
    // 햄버거 메뉴판 바깥 클릭 방지
    function handleClickOutside(event: MouseEvent) {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setIsMenuOpen(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [menuRef]);

  return (
    <div className={styles.container}>
      {/* Header */}
      <div className={styles.header}>
        <LeftarrowSVG
          fill="blue"
          onClick={() => {
            navigate(-1);
          }}
        />
        <div className={styles.userinfo}>조현수</div>
        <button
          onClick={() => setIsMenuOpen(!isMenuOpen)}
          className={styles.hamburgerButton}
        >
          <HamburgerSVG />
        </button>
      </div>
      {/* 채팅 내용 */}
      <ChatContent />
      {/* 채팅 입력창 */}
      <div className={styles.chatinput}>
        <textarea
          className={styles.inputField}
          placeholder="메세지를 입력하세요."
          //   value={message}
          //   onChange={(e) => setMessage(e.target.value)}
          //   onKeyDown={(e) => e.key === 'Enter' && !e.shiftKey && handleSend()}
          rows={1}
        />
        <SendButton />
      </div>
      {/* 오버레이 */}
      {isMenuOpen && (
        <div className={styles.overlay} onClick={() => setIsMenuOpen(false)} />
      )}
      {/* 메뉴 */}
      <div ref={menuRef}>
        <Menu
          isOpen={isMenuOpen}
          onClose={() => setIsMenuOpen(false)}
          onOpenModal={handleOpenModal}
        />
      </div>

      {/* Modals */}
      {modalType === 'create' && (
        <CreateIouModal
          isOpen={modalType === 'create'}
          onClose={handleCloseModal}
        />
      )}
      {modalType === 'correction' && (
        <CorrectionIouModal
          isOpen={modalType === 'correction'}
          onClose={handleCloseModal}
        />
      )}

      {modalType === 'check' && (
        <CheckIouModal
          isOpen={modalType === 'check'}
          onClose={handleCloseModal}
        />
      )}
    </div>
  );
}
