/* eslint-disable @typescript-eslint/indent */
import { useState, useEffect, useCallback } from 'react';

import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

import { ChatMessage } from '@/types/ChatMessage';

export function useChatWebSocket(
  roomId: string,
  senderId: string,
): {
  messages: ChatMessage[];
  sendMessage: (content: string) => void;
} {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [stompClient, setStompClient] = useState<Client | null>(null);

  useEffect(() => {
    const socket = new SockJS('https://localhost:8080/ws');
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        console.log('Connected to WebSocket');
        setStompClient(client);

        client.subscribe(`/topic/chat.${roomId}`, (message: Message) => {
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
  }, [roomId]);

  const sendMessage = useCallback(
    (content: string) => {
      if (stompClient && stompClient.active) {
        const chatMessage: ChatMessage = {
          chatRoomId: roomId,
          senderId,
          content,
          messageType: 'TALK',
        };
        stompClient.publish({
          destination: '/pub/chat.message',
          body: JSON.stringify(chatMessage),
        });
      } else {
        console.error('STOMP client is not connected');
      }
    },
    [stompClient, roomId, senderId],
  );

  return { messages, sendMessage };
}
