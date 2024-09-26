import { useState } from 'react';

import { Input } from '@/components/Input/Input';
import { FILTER, STATUS } from '@/constants/Chatting';
import { ChatRoom } from '@/types/Chat';

import { Chatting } from './Chatting';
import styles from './Main.module.scss';

export function Main() {
  const [filter, setFilter] = useState(FILTER.CREDIT);

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
      unreadCount: 3,
    },
    {
      chatRoomId: 1,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'OVERDUE',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: 20,
      unreadCount: 3,
    },
    {
      chatRoomId: 1,
      name: '강민서',
      avatarUrl: '',
      contractStatus: 'COMPLETED',
      lastMessage: '지금 바쁨',
      iouAmount: 300000,
      daysUntilDue: 20,
      unreadCount: 3,
    },
  ];

  const handleCreditClick = () => {
    setFilter(FILTER.CREDIT);
  };

  const handleDebtClick = () => {
    setFilter(FILTER.DEBT);
  };

  return (
    <div className={styles.container}>
      <div>
        <div>
          <button
            onClick={handleCreditClick}
            className={`${styles.filterButton} ${filter === FILTER.CREDIT && styles.active}`}
          >
            빌려준 기록
          </button>
          <div />
          <button
            onClick={handleDebtClick}
            className={`${styles.filterButton} ${filter === FILTER.DEBT && styles.active}`}
          >
            빌린 기록
          </button>
        </div>
        <Input type="search" placeholder="사람 이름 검색" />
      </div>
      <div>
        <div>
          <div>거래 중</div>
          <div>
            {chatList &&
              chatList
                .filter((chat) => chat.contractStatus === STATUS.ACTIVE)
                .map((chat) => <Chatting chat={chat} key={chat.chatRoomId} />)}
          </div>
        </div>
        <div>
          <div>연체</div>
          <ul>
            {chatList &&
              chatList
                .filter((chat) => chat.contractStatus === STATUS.OVERDUE)
                .map((chat) => <Chatting chat={chat} key={chat.chatRoomId} />)}
          </ul>
        </div>
        <div>
          <div>거래 완료</div>
          <div>
            {chatList &&
              chatList
                .filter((chat) => chat.contractStatus === STATUS.COMPLETED)
                .map((chat) => <Chatting chat={chat} key={chat.chatRoomId} />)}
          </div>
        </div>
      </div>
    </div>
  );
}
