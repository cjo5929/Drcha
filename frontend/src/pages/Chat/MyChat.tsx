import styles from './Chat.module.scss';

export function MyChat({ content }: { content: string }) {
  return (
    <div className={styles.mychat}>
      <div className={styles.mybubble}>
        {content}
        <div className={styles.mybubblearrow} />
      </div>
    </div>
  );
}
