import { ChatMessage } from '@/types/ChatMessage';

import styles from './Chat.module.scss';
import { Doctor } from './Doctor';
import { MyChat } from './MyChat';
import { PartnerChat } from './PartnerChat';

export function ChatContent({
  messages,
  currentUserId,
}: {
  messages: ChatMessage[];
  currentUserId: string;
}) {
  return (
    <div className={styles.chatcontainer}>
      {messages.map((message: ChatMessage) => {
        switch (message.messageType) {
          case 'SYSTEM':
            return <Doctor content={message.content} />;
          case 'TALK':
            if (message.senderId === currentUserId) {
              return <MyChat content={message.content} />;
            }
            return <PartnerChat content={message.content} />;

          default:
            return null;
        }
      })}
    </div>
  );
}
