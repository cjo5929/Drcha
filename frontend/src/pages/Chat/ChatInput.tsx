import styles from './Chat.module.scss';

export function ChatInput() {
  return (
    <div className={styles.chatinput}>
      <button>+</button>
      <input />
    </div>
  );
}
