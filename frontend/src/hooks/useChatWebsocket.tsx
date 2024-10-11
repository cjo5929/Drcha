/* eslint-disable @typescript-eslint/indent */
import { useState, useEffect, useCallback } from 'react';

import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

import { baseURL } from '@/services/api';
import { chat } from '@/services/chat';
import { ChatMessage, SendChatMessage } from '@/types/ChatMessage';
import { IouData } from '@/types/iou';

export function useChatWebSocket(chatRoomId: string): {
  messages: ChatMessage[];
  sendMessage: (contents: string, senderId: number) => void;
  setMessages: React.Dispatch<React.SetStateAction<ChatMessage[]>>;
  latestIOU: IouData | null;
  setLatestIOU: React.Dispatch<React.SetStateAction<IouData | null>>;
} {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [latestIOU, setLatestIOU] = useState<IouData | null>(null);

  useEffect(() => {
    const socket = new SockJS(`${baseURL}/wss`);
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        setStompClient(client);

        client.subscribe(`/topic/chat.${chatRoomId}`, (message: Message) => {
          const newMessage = JSON.parse(message.body) as ChatMessage;
          setMessages((prevMessages) => {
            const updatedMessages = [...prevMessages, newMessage];
            const latestIOUMessage = updatedMessages
              .filter((m) => m.messageType === 'IOU')
              .pop();
            if (latestIOUMessage) {
              setLatestIOU(latestIOUMessage.iouInfo);
            }
            return updatedMessages;
          });
        });
      },
    });

    client.activate();

    return () => {
      if (client.active) {
        chat.leaveChatRoom(chatRoomId);
        client.deactivate();
      }
    };
  }, [chatRoomId]);

  const sendMessage = useCallback(
    (contents: string, senderId: number) => {
      if (stompClient && stompClient.active) {
        const chatMessage: SendChatMessage = {
          chatRoomId,
          senderId,
          content: contents,
          messageType: 'TALK',
        };
        stompClient.publish({
          destination: '/pub/chat.message',
          body: JSON.stringify(chatMessage),
        });
      }
    },
    [stompClient, chatRoomId],
  );

  return { messages, setMessages, sendMessage, latestIOU, setLatestIOU };
}
