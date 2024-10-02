import styles from './Chat.module.scss';

export function PartnerChat({ content }: { content: string }) {
  return (
    <div className={styles.partnerchat}>
      <div className={styles.partnerbubble}>
        <div className={styles.partnerbubblearrow} />
        {content}
      </div>
    </div>
  );
}
