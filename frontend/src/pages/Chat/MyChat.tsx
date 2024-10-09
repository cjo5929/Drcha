import styles from './Chat.module.scss';

export function MyChat({
  content,
  timestamp,
}: {
  content: string;
  timestamp: string;
}) {
  return (
    <div className={styles.mychat}>
      <div className={styles.timestamp}>{timestamp}</div>
      <div className={styles.mybubble}>
        {content}
        <div className={styles.mybubblearrow} />
      </div>
    </div>
  );
}
