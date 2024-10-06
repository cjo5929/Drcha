import { useEffect, useState } from 'react';
import { NavLink } from 'react-router-dom';

import AVATAR_IMAGE from '@/assets/images/avatar.png';
import { STATUS } from '@/constants/chatting';
import { ChatRoom } from '@/types/Chat';

import styles from './Main.module.scss';

type ChattingProps = {
  chat: ChatRoom;
};

export function Chatting({ chat }: ChattingProps) {
  const {
    chatRoomId,
    name,
    avatarUrl,
    contractStatus,
    lastMessage,
    iouAmount,
    daysUntilDue,
    unreadCount,
  } = chat;
  const [status, setStatus] = useState('');

  useEffect(() => {
    if (contractStatus === STATUS.ACTIVE) {
      setStatus(`D-${daysUntilDue}일`);
    } else if (contractStatus === STATUS.OVERDUE) {
      setStatus(`${-1 * daysUntilDue}일 연체중`);
    } else if (contractStatus === STATUS.DRAFTING) {
      setStatus('거래 중');
    } else {
      setStatus('상환 완료');
    }
  }, [contractStatus, daysUntilDue]);

  return (
    <NavLink to={`chat/${chatRoomId}`} className={styles.chatting}>
      {unreadCount > 0 && (
        <div className={styles.unreadCount}>{unreadCount}</div>
      )}
      <div className={styles.chattingBody}>
        <img
          src={avatarUrl || AVATAR_IMAGE}
          alt={name}
          className={styles.avatarImage}
        />
        <div className={styles.chattingContent}>
          <div className={styles.name}>{name}</div>
          <div className={styles.chat}>{lastMessage || ''}</div>
        </div>
      </div>
      <div className={styles.chatStatus}>
        <div className={styles.statusDetail}>
          {contractStatus === STATUS.ACTIVE && (
            <span className={styles.statusDday}>상환일까지 </span>
          )}
          {status}
        </div>
        <div>{iouAmount ? `${iouAmount.toLocaleString('ko-KR')}원` : ''}</div>
      </div>
    </NavLink>
  );
}
