import { useEffect, useState } from 'react';
import { NavLink } from 'react-router-dom';

import { STATUS } from '@/constants/Chatting';
import { ChatRoom } from '@/types/Chat';

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
  } = chat;
  const [status, setStatus] = useState('');

  useEffect(() => {
    if (contractStatus === STATUS.ACTIVE) {
      setStatus(`상환일까지 D-${daysUntilDue}일`);
    } else if (contractStatus === STATUS.OVERDUE) {
      setStatus(`${-1 * daysUntilDue}일 연체중`);
    } else {
      setStatus('상환 완료');
    }
  }, [contractStatus, daysUntilDue]);

  return (
    <NavLink to={`chat/${chatRoomId}`}>
      <img src={avatarUrl} alt={name} />
      <div>
        <div>{name}</div>
        <div>{lastMessage}</div>
      </div>
      <div>
        <div>{status}</div>
        <div>{`${iouAmount}원`}</div>
      </div>
    </NavLink>
  );
}
