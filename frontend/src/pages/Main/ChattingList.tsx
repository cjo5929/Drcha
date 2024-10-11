import DownArrowSVG from '@/assets/icons/downarrow.svg?react';
import { ChatRoom } from '@/types/Chat';

import { Chatting } from './Chatting';
import styles from './Main.module.scss';

type ChattingListProps = {
  name: string;
  chattings: ChatRoom[];
  status: boolean;
  setStatus: React.Dispatch<React.SetStateAction<boolean>>;
};

export function ChattingList({
  name,
  chattings,
  status,
  setStatus,
}: ChattingListProps) {
  return (
    <div className={styles.status}>
      <button
        className={styles.statusButton}
        onClick={() => setStatus((prev) => !prev)}
      >
        <DownArrowSVG
          className={`${styles.arrow} ${status !== true && styles.rotate}`}
        />
        {name} {chattings.length}
      </button>
      <div className={styles.chattingList}>
        {chattings &&
          status &&
          chattings.map((chat) => (
            <Chatting chat={chat} key={chat.chatRoomId} />
          ))}
      </div>
    </div>
  );
}
