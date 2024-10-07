/* eslint-disable @typescript-eslint/indent */
import { useState, useRef, useEffect, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import HamburgerSVG from '@/assets/icons/hamburger.svg?react';
import LeftarrowSVG from '@/assets/icons/leftArrow.svg?react';
import { CheckIouModal } from '@/components/Modal/CheckIouModal';
import { CorrectionIouModal } from '@/components/Modal/CorrectionIouModal';
import { CreateIouModal } from '@/components/Modal/CreateIouModal';
import { useChatWebSocket } from '@/hooks/useChatWebsocket';
import { useUserState } from '@/hooks/useUserState';
import { chat } from '@/services/chat';

import styles from './Chat.module.scss';
import { ChatContent } from './ChatContent';
import { Menu } from './Menu';
import { SendButton } from './SendButton';

export function Chat() {
  const { chatRoomId } = useParams<{ chatRoomId: string }>();
  const textAreaRef = useRef<HTMLTextAreaElement | null>(null);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [modalType, setModalType] = useState<
    'create' | 'correction' | 'check' | null
  >(null);
  const menuRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const [message, setMessage] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const { userInfo } = useUserState();
  const { messages, sendMessage } = useChatWebSocket(chatRoomId!);
  const [opponentName, setOpponentName] = useState('');
  const [isError, setIsError] = useState(false);

  const handleOpenModal = (type: 'create' | 'correction' | 'check') => {
    setModalType(type);
  };

  const handleCloseModal = () => {
    setModalType(null);
  };

  const handleSend = useCallback(
    (text: string) => {
      if (!userInfo) {
        return;
      }
      if (text && textAreaRef.current) {
        sendMessage(text, userInfo.memberId);
        setMessage('');
        textAreaRef.current.value = '';
      }
      // eslint-disable-next-line react-hooks/exhaustive-deps
    },
    [sendMessage, userInfo],
  );

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend(message);
    }
  };

  const getChatRoomInfo = useCallback(async () => {
    if (chatRoomId) {
      try {
        const data = await chat.getChatRoomData(chatRoomId);
        setOpponentName(data.opponentName);
        setIsLoading(true);
      } catch {
        setIsError(true);
      } finally {
        setIsLoading(false);
      }
    }
  }, [chatRoomId]);

  useEffect(() => {
    if (!userInfo) {
      setIsLoading(true);
    } else {
      setIsLoading(false);
      getChatRoomInfo();
    }
  }, [userInfo, getChatRoomInfo]);

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
    return <div className={styles.noChatting}>채팅 정보 로딩 중..</div>;
  }

  if (isError) {
    return <div className={styles.noChatting}>채팅방을 찾을 수 없습니다.</div>;
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
        <div className={styles.userinfo}>{opponentName}</div>
        <button
          onClick={() => setIsMenuOpen(!isMenuOpen)}
          className={styles.hamburgerButton}
        >
          <HamburgerSVG />
        </button>
      </div>
      {/* 채팅 내용 */}
      <ChatContent messages={messages} currentUserId={userInfo!.memberId} />
      {/* 채팅 입력창 */}
      <div className={styles.chatinput}>
        <textarea
          ref={textAreaRef}
          className={styles.inputField}
          placeholder="메세지를 입력하세요."
          // value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyDown={handleKeyDown}
          rows={1}
        />
        <SendButton onClick={() => handleSend(message)} />
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
