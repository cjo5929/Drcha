import { useEffect, useRef } from 'react';

import { IOUContent } from '@/components/IOU/IOUContent';
import { Loading } from '@/components/Loading/Loading';
import { ChatMessage } from '@/types/ChatMessage';

import styles from './Chat.module.scss';
import { Doctor } from './Doctor';
import { MyChat } from './MyChat';
import { PartnerChat } from './PartnerChat';

export function ChatContent({
  messages,
  currentUserId,
  isLoading,
}: {
  messages: ChatMessage[];
  currentUserId: number;
  isLoading: boolean;
}) {
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const formatTime = (createdAt: string) => {
    const date = new Date(createdAt);
    return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
  };

  if (isLoading) {
    return <Loading size={100} />;
  }

  return (
    <div className={styles.chatcontainer}>
      {messages.map((message: ChatMessage) => {
        if (message.messageType === 'IOU') {
          return (
            <Doctor key={message.id}>
              <div className={styles.iouContainer}>
                <IOUContent iouData={message.iouInfo} type="chat" />
              </div>
            </Doctor>
          );
        }
        if (
          message.messageType === 'SYSTEM' ||
          message.messageType === 'ENTER'
        ) {
          return <Doctor key={message.id} content={message.content} />;
        }
        if (message.messageType === 'TALK') {
          if (message.senderId === currentUserId) {
            return (
              <MyChat
                key={message.id}
                content={message.content}
                timestamp={formatTime(message.createdAt)}
              />
            );
          }
          return (
            <PartnerChat
              key={message.id}
              content={message.content}
              timestamp={formatTime(message.createdAt)}
            />
          );
        }
        return null;
      })}
      <div ref={messagesEndRef} />
    </div>
  );
}
