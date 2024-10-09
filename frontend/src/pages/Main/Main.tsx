import { useCallback, useEffect, useRef, useState } from 'react';

import { Input } from '@/components/Input/Input';
import { Loading } from '@/components/Loading/Loading';
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
  const [visibleChatList, setVisibleChatList] = useState<ChatRoom[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);
  const searchInputRef = useRef<HTMLInputElement>(null);

  const getChattingLists = useCallback(async () => {
    setIsLoading(true);
    try {
      if (filter === FILTER.DEBT) {
        const newChattingList = await chat.getBorrowedChattings();
        setChatList(newChattingList);
        return;
      }
      const newChattingList = await chat.getLentChattings();
      setChatList(newChattingList);
    } catch {
      setIsError(true);
    } finally {
      if (searchInputRef.current) {
        searchInputRef.current.value = '';
      }
      setIsLoading(false);
    }
  }, [filter]);

  useEffect(() => {
    getChattingLists();
  }, [getChattingLists]);

  useEffect(() => {
    setVisibleChatList(chatList);
  }, [chatList, isError]);

  useEffect(() => {
    setActiveChat(
      visibleChatList.filter(
        (chatting) =>
          chatting.contractStatus === STATUS.ACTIVE ||
          chatting.contractStatus === STATUS.DRAFTING,
      ),
    );
    setCompletedChat(
      visibleChatList.filter(
        (chatting) => chatting.contractStatus === STATUS.COMPLETED,
      ),
    );
    setOverdueChat(
      visibleChatList.filter(
        (chatting) => chatting.contractStatus === STATUS.OVERDUE,
      ),
    );
  }, [visibleChatList]);

  const handleCreditClick = () => {
    setFilter(FILTER.CREDIT);
  };

  const handleDebtClick = () => {
    setFilter(FILTER.DEBT);
  };

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setVisibleChatList(
      chatList.filter((chatting) => chatting.name.includes(value)),
    );
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
        <Input
          type="search"
          placeholder="사람 이름 검색"
          onChange={handleSearch}
          ref={searchInputRef}
        />
      </div>
      {!isError &&
        (isLoading ? (
          <Loading size={100} />
        ) : (
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
        ))}
      {isError && (
        <div className={styles.error}>채팅 목록을 불러올 수 없습니다</div>
      )}
      <Navbar />
    </div>
  );
}
