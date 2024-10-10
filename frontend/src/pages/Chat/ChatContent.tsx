import { useEffect, useState, useRef, useCallback } from 'react';

import { Button } from '@/components/Button/Button';
import { IOUContent } from '@/components/IOU/IOUContent';
import { Loading } from '@/components/Loading/Loading';
import { iou } from '@/services/iou';
import { ChatMessage } from '@/types/ChatMessage';
import { IouData } from '@/types/iou';

import styles from './Chat.module.scss';
import { Doctor } from './Doctor';
import { MyChat } from './MyChat';
import { PartnerChat } from './PartnerChat';

export function ChatContent({
  messages,
  currentUserId,
  isLoading,
  memberRole,
}: {
  messages: ChatMessage[];
  currentUserId: number;
  isLoading: boolean;
  memberRole: string | undefined;
}) {
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const [localAgreement, setLocalAgreement] = useState<string | null>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const handleCheck = useCallback(async (iouInfo: IouData) => {
    if (iouInfo) {
      setLocalAgreement(String(iouInfo.iouId));
      await iou.agreeIou(String(iouInfo.iouId));
    }
  }, []);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const latestIOU = messages.filter((m) => m.messageType === 'IOU').pop();

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
        if (message.messageType === 'IOU' && message === latestIOU) {
          const isAgreed =
            localAgreement === String(message.iouInfo.iouId) ||
            (memberRole === 'DEBTOR'
              ? message.iouInfo.borrowerAgreement
              : message.iouInfo.lenderAgreement);
          return (
            <Doctor key={message.id}>
              <div className={styles.iouContainer}>
                <IOUContent iouData={message.iouInfo} type="chat" />
              </div>
              {isAgreed ? (
                <div className={styles.modalIOUExplain}>
                  이미 동의한 차용증입니다.
                </div>
              ) : (
                <>
                  <div className={styles.modalIOUExplain}>
                    상기의 내용을 확인하였으며,
                    <br />
                    위의 거래에 동의하십니까?
                  </div>
                  <div className={styles.modalBtns}>
                    <Button
                      color="lightblue"
                      size="small"
                      onClick={() => {
                        if (message.iouInfo) {
                          handleCheck(message.iouInfo);
                        }
                      }}
                    >
                      동의
                    </Button>
                  </div>
                </>
              )}
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
