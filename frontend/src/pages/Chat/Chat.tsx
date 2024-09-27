import styles from './Chat.module.scss';
import { ChatContent } from './ChatContent';
import { ChatInput } from './ChatInput';
import { Header } from './Header';

export function Chat() {
  return (
    <div className={styles.container}>
      <Header />
      <ChatContent />
      <ChatInput />
    </div>
  );
}
