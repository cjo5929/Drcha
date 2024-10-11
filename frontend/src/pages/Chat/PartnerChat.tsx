import styles from './Chat.module.scss';

export function PartnerChat({
  content,
  timestamp,
}: {
  content: string;
  timestamp: string;
}) {
  return (
    <div className={styles.partnerchat}>
      <div className={styles.partnerbubble}>
        <div className={styles.partnerbubblearrow} />
        {content}
      </div>
      <div className={styles.timestamp}>{timestamp}</div>
    </div>
  );
}
