import { useEffect, useRef } from 'react';

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
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  return (
    <div className={styles.chatcontainer}>
      {messages.map((message: ChatMessage) => {
        switch (message.messageType) {
          case 'SYSTEM' || 'ENTER':
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
      <div ref={messagesEndRef} />
    </div>
  );
}
