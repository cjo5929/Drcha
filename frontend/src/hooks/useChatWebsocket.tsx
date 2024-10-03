/* eslint-disable @typescript-eslint/indent */
import { useState, useEffect, useCallback } from 'react';

import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

import { ChatMessage } from '@/types/ChatMessage';

export function useChatWebSocket(
  thisroomId: string,
  thissenderId: string,
): {
  messages: ChatMessage[];
  sendMessage: (contents: string) => void;
} {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [stompClient, setStompClient] = useState<Client | null>(null);

  useEffect(() => {
    const socket = new SockJS('https://j11a205.p.ssafy.io/wss');
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        setStompClient(client);

        client.subscribe(`/topic/chat.${thisroomId}`, (message: Message) => {
          const newMessage = JSON.parse(message.body) as ChatMessage;
          setMessages((prevMessages) => [...prevMessages, newMessage]);
        });
      },
    });

    client.activate();

    return () => {
      if (client.active) {
        client.deactivate();
      }
    };
  }, [thisroomId]);

  const sendMessage = useCallback(
    (contents: string) => {
      if (stompClient && stompClient.active) {
        const chatMessage: ChatMessage = {
          chatRoomId: thisroomId,
          senderId: thissenderId,
          content: contents,
          messageType: 'TALK',
        };
        stompClient.publish({
          destination: '/pub/chat.message',
          body: JSON.stringify(chatMessage),
        });
      }
    },
    [stompClient, thisroomId, thissenderId],
  );

  return { messages, sendMessage };
}
