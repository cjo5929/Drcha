/* eslint-disable @typescript-eslint/indent */
import { useState, useEffect, useCallback } from 'react';

import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

import { baseURL } from '@/services/api';
import { ChatMessage } from '@/types/ChatMessage';

export function useChatWebSocket(chatRoomId: string): {
  messages: ChatMessage[];
  sendMessage: (contents: string, senderId: number) => void;
} {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [stompClient, setStompClient] = useState<Client | null>(null);

  useEffect(() => {
    const socket = new SockJS(`${baseURL}/wss`);
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        setStompClient(client);

        client.subscribe(`/topic/chat.${chatRoomId}`, (message: Message) => {
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
  }, [chatRoomId]);

  const sendMessage = useCallback(
    (contents: string, senderId: number) => {
      if (stompClient && stompClient.active) {
        const chatMessage: ChatMessage = {
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

  return { messages, sendMessage };
}
