import styles from './Chat.module.scss';
import { ChatContent } from './ChatContent';
import { Header } from './Header';

export function Chat() {
  return (
    <div className={styles.container}>
      <Header />
      <ChatContent />
      <div className={styles.chatinput}>
        <textarea
          className={styles.inputField}
          placeholder="Type your message..."
          //   value={message}
          //   onChange={(e) => setMessage(e.target.value)}
          //   onKeyDown={(e) => e.key === 'Enter' && !e.shiftKey && handleSend()}
          rows={1}
        />
        <button className={styles.sendButton}>➤</button>
      </div>
    </div>
  );
}
