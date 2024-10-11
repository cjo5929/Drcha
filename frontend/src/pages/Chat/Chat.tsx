/* eslint-disable @typescript-eslint/indent */
import { useState, useRef, useEffect, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import axios from 'axios';

import HamburgerSVG from '@/assets/icons/hamburger.svg?react';
import LeftarrowSVG from '@/assets/icons/leftArrow.svg?react';
import { CheckIouModal } from '@/components/Modal/CheckIouModal';
import { CorrectionIouModal } from '@/components/Modal/CorrectionIouModal';
import { CreateIouModal } from '@/components/Modal/CreateIouModal';
import { useChatWebSocket } from '@/hooks/useChatWebsocket';
import { useUserState } from '@/hooks/useUserState';
import { chat } from '@/services/chat';
import { iou } from '@/services/iou';
import { ChatRoomSummary } from '@/types/Chat';
import { IouDatainChatroom, ManualIouData } from '@/types/iou';

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
  const { messages, setMessages, sendMessage, latestIOU, setLatestIOU } =
    useChatWebSocket(chatRoomId!);
  const [isError, setIsError] = useState(false);
  const [curIou, setCurIou] = useState<IouDatainChatroom[]>([]);
  const [chatRoomData, setChatRoomData] = useState<ChatRoomSummary>();

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

  const createIou = useCallback(async () => {
    if (chatRoomId) {
      await iou.createIou(chatRoomId);
    }
  }, [chatRoomId]);

  const getIou = useCallback(async () => {
    if (chatRoomId) {
      try {
        setIsLoading(true);
        const data = await iou.getIou(chatRoomId);
        setCurIou(data);
      } catch {
        setIsError(true);
      } finally {
        setIsLoading(false);
      }
    }
  }, [chatRoomId]);

  const createManualIou = useCallback(
    async (
      iouAmount: number,
      interestRate: number,
      contractEndDate: string,
    ) => {
      if (chatRoomId) {
        const data: ManualIouData = {
          chatRoomId,
          iouAmount,
          interestRate,
          contractEndDate,
        };
        await iou.createManualIou(data);
      }
    },
    [chatRoomId],
  );

  const enterChatRoom = useCallback(
    async (creditorId: string) => {
      if (!chatRoomId || !userInfo) {
        return;
      }
      if (userInfo.memberId !== Number(creditorId)) {
        try {
          setIsLoading(true);
          await chat.enterChatRoom(chatRoomId);
          window.location.reload();
        } catch (error) {
          setIsError(true);
        } finally {
          setIsLoading(false);
        }
      }
    },
    [chatRoomId, userInfo],
  );

  const getChatRoomInfo = useCallback(async () => {
    if (chatRoomId) {
      if (userInfo && !userInfo.verified) {
        navigate(`/auth/account?chatRoomId=${chatRoomId}`);
        return;
      }
      try {
        const data = await chat.getChatRoomData(chatRoomId);
        if (
          data.creditorId !== userInfo?.memberId &&
          data.debtorId !== userInfo?.memberId
        ) {
          setIsError(true);
          return;
        }
        setChatRoomData(data);
        setIsLoading(true);
      } catch (error) {
        if (axios.isAxiosError(error)) {
          if (error.response?.status === 400) {
            await enterChatRoom(error.response.data.errorMessage);
          }
        }
        setIsError(true);
      } finally {
        setIsLoading(false);
      }
    }
  }, [navigate, chatRoomId, userInfo, enterChatRoom]);

  const getExistingMessages = useCallback(async () => {
    if (chatRoomId) {
      try {
        setIsLoading(true);
        const existingMessages = await chat.getAllMessages(chatRoomId);
        setMessages(existingMessages);
        const latestIOUMessage = existingMessages
          .filter((m) => m.messageType === 'IOU')
          .pop();
        if (latestIOUMessage) {
          setLatestIOU(latestIOUMessage.iouInfo);
        }
      } catch (error) {
        setIsError(true);
      } finally {
        setIsLoading(false);
      }
    }
  }, [chatRoomId, setLatestIOU, setMessages]);

  const handleChatRoomEnter = useCallback(async () => {
    await getChatRoomInfo();
    await getExistingMessages();
  }, [getChatRoomInfo, getExistingMessages]);

  useEffect(() => {
    if (!userInfo) {
      setIsLoading(true);
    } else {
      setIsLoading(false);
      handleChatRoomEnter();
      getIou();
    }
  }, [userInfo, handleChatRoomEnter, getIou]);

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

  if (isError) {
    return <div className={styles.noChatting}> </div>;
  }

  return (
    <div className={styles.container}>
      {/* Header */}
      <div className={styles.header}>
        <LeftarrowSVG
          fill="blue"
          onClick={() => {
            navigate('/');
          }}
          className={styles.leftarrowbtn}
        />
        <div className={styles.userinfo}>
          {chatRoomData && chatRoomData.opponentName}
        </div>
        <button
          onClick={() => setIsMenuOpen(!isMenuOpen)}
          className={styles.hamburgerButton}
        >
          <HamburgerSVG />
        </button>
      </div>
      {/* 채팅 내용 */}
      {userInfo && (
        <ChatContent
          messages={messages}
          currentUserId={userInfo.memberId}
          isLoading={isLoading}
          memberRole={chatRoomData?.memberRole}
        />
      )}
      {/* 채팅 입력창 */}
      <div className={styles.chatinput}>
        <textarea
          ref={textAreaRef}
          className={styles.inputField}
          placeholder="메세지를 입력하세요."
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
          curIou={curIou}
          memberRole={chatRoomData?.memberRole}
        />
      </div>

      {/* Modals */}
      {modalType === 'create' && (
        <CreateIouModal
          isOpen={modalType === 'create'}
          onClose={handleCloseModal}
          createIou={createIou}
        />
      )}
      {modalType === 'correction' && (
        <CorrectionIouModal
          isOpen={modalType === 'correction'}
          onClose={handleCloseModal}
          createManualIou={createManualIou}
        />
      )}

      {modalType === 'check' && (
        <CheckIouModal
          isOpen={modalType === 'check'}
          onClose={handleCloseModal}
          activeIou={latestIOU}
          memberRole={chatRoomData?.memberRole}
        />
      )}
    </div>
  );
}
