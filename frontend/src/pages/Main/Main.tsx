import { useEffect, useState } from 'react';

import { Input } from '@/components/Input/Input';
import { Navbar } from '@/components/Navbar/Navbar';
import { FILTER, STATUS } from '@/constants/chatting';
import { ChatRoom } from '@/types/Chat';

import { ChattingList } from './ChattingList';
import styles from './Main.module.scss';

export function Main() {
  const [filter, setFilter] = useState(FILTER.CREDIT);
  const [activeChat, setActiveChat] = useState<ChatRoom[]>([]);
  const [completedChat, setCompletedChat] = useState<ChatRoom[]>([]);
  const [overdueChat, setOverdueChat] = useState<ChatRoom[]>([]);
  const [showActive, setShowActive] = useState(true);
  const [showCompleted, setShowCompleted] = useState(true);
  const [showOverdue, setShowOverdue] = useState(true);

  // TODO : 채팅 정보 불러오기
  const chatList: ChatRoom[] = [
    {
      chatRoomId: 1,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'ACTIVE',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: 20,
      unreadCount: 300,
    },
    {
      chatRoomId: 2,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'ACTIVE',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: 20,
      unreadCount: 300,
    },
    {
      chatRoomId: 3,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'ACTIVE',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: 20,
      unreadCount: 300,
    },
    {
      chatRoomId: 4,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'OVERDUE',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: -20,
      unreadCount: 3,
    },
    {
      chatRoomId: 5,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'OVERDUE',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: -20,
      unreadCount: 3,
    },
    {
      chatRoomId: 6,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'OVERDUE',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: -20,
      unreadCount: 3,
    },
    {
      chatRoomId: 7,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'COMPLETED',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: 20,
      unreadCount: 3,
    },
    {
      chatRoomId: 8,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'COMPLETED',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: 20,
      unreadCount: 3,
    },
    {
      chatRoomId: 9,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'COMPLETED',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: 20,
      unreadCount: 3,
    },
  ];

  useEffect(() => {
    setActiveChat(
      chatList.filter((chat) => chat.contractStatus === STATUS.ACTIVE),
    );
    setCompletedChat(
      chatList.filter((chat) => chat.contractStatus === STATUS.COMPLETED),
    );
    setOverdueChat(
      chatList.filter((chat) => chat.contractStatus === STATUS.OVERDUE),
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleCreditClick = () => {
    setFilter(FILTER.CREDIT);
  };

  const handleDebtClick = () => {
    setFilter(FILTER.DEBT);
  };

  return (
    <div className={styles.container}>
      <div className={styles.mainHeader}>
        <div className={styles.filter}>
          <button
            onClick={handleCreditClick}
            className={`${styles.filterButton} ${filter === FILTER.CREDIT && styles.active}`}
          >
            빌려준 기록
          </button>
          <div className={styles.divider} />
          <button
            onClick={handleDebtClick}
            className={`${styles.filterButton} ${filter === FILTER.DEBT && styles.active}`}
          >
            빌린 기록
          </button>
        </div>
        <Input type="search" placeholder="사람 이름 검색" />
      </div>
      <div className={styles.mainContent}>
        <ChattingList
          name="거래 중"
          chattings={activeChat}
          status={showActive}
          setStatus={setShowActive}
        />
        <ChattingList
          name="연체"
          chattings={overdueChat}
          status={showOverdue}
          setStatus={setShowOverdue}
        />
        <ChattingList
          name="거래 완료"
          chattings={completedChat}
          status={showCompleted}
          setStatus={setShowCompleted}
        />
      </div>
      <Navbar />
    </div>
  );
}
