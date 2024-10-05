import { useCallback, useEffect, useState } from 'react';

import { Input } from '@/components/Input/Input';
import { Navbar } from '@/components/Navbar/Navbar';
import { FILTER, STATUS } from '@/constants/chatting';
import { chat } from '@/services/chat';
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
  const [chatList, setChatList] = useState<ChatRoom[]>([]);

  const getChattingLists = useCallback(async () => {
    if (filter === FILTER.CREDIT) {
      const newChattingList = await chat.getBorrowedChattings();
      setChatList(newChattingList);
      return;
    }
    const newChattingList = await chat.getLentChattings();
    setChatList(newChattingList);
  }, [filter]);

  useEffect(() => {
    getChattingLists();
  }, [getChattingLists]);

  useEffect(() => {
    setActiveChat(
      chatList.filter(
        (chatting) =>
          chatting.contractStatus === STATUS.ACTIVE || STATUS.DRAFTING,
      ),
    );
    setCompletedChat(
      chatList.filter(
        (chatting) => chatting.contractStatus === STATUS.COMPLETED,
      ),
    );
    setOverdueChat(
      chatList.filter((chatting) => chatting.contractStatus === STATUS.OVERDUE),
    );
  }, [chatList]);

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
