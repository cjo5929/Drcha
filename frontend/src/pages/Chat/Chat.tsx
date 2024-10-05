/* eslint-disable @typescript-eslint/indent */
import { useState, useRef, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import HamburgerSVG from '@/assets/icons/hamburger.svg?react';
import LeftarrowSVG from '@/assets/icons/leftArrow.svg?react';
import { CheckIouModal } from '@/components/Modal/CheckIouModal';
import { CorrectionIouModal } from '@/components/Modal/CorrectionIouModal';
import { CreateIouModal } from '@/components/Modal/CreateIouModal';
import { useChatWebSocket } from '@/hooks/useChatWebsocket';
import { API } from '@/services/api';
import { MyInfo } from '@/types/MyInfo';

import styles from './Chat.module.scss';
import { ChatContent } from './ChatContent';
import { Menu } from './Menu';
import { SendButton } from './SendButton';

export function Chat() {
  const { chatRoomId } = useParams<{ chatRoomId: string }>();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [modalType, setModalType] = useState<
    'create' | 'correction' | 'check' | null
  >(null);
  const menuRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const [message, setMessage] = useState('');
  const [myData, setMyData] = useState<MyInfo | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const { messages, sendMessage } = useChatWebSocket(
    chatRoomId ?? '',
    myData!.username ?? '',
  );

  const handleOpenModal = (type: 'create' | 'correction' | 'check') => {
    setModalType(type);
  };

  const handleCloseModal = () => {
    setModalType(null);
  };

  const handleSend = () => {
    const trimmedMessage = message.trim().replace(/\n/g, ' ');
    if (trimmedMessage) {
      sendMessage(trimmedMessage);
      setMessage('');
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  useEffect(() => {
    const fetchMemberInfo = async () => {
      try {
        setIsLoading(true);
        const response = await API.get<MyInfo>('/member/info');

        setMyData(response.data);
      } catch (e) {
        setError('Failed to fetch member info');
      } finally {
        setIsLoading(false);
      }
    };

    fetchMemberInfo();
  }, []);

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

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }
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
      <ChatContent messages={messages} currentUserId="2" />
      {/* 채팅 입력창 */}
      <div className={styles.chatinput}>
        <textarea
          className={styles.inputField}
          placeholder="메세지를 입력하세요."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyDown={handleKeyDown}
          rows={1}
        />
        <SendButton onClick={handleSend} />
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
