import SendMessageSVG from '@/assets/icons/sendMessage.svg?react';

import styles from './Chat.module.scss';

interface SendButtonProps {
  onClick: () => void;
}

export function SendButton({ onClick }: SendButtonProps) {
  return (
    <button onClick={onClick} className={styles.sendButton}>
      <SendMessageSVG />
    </button>
  );
}
