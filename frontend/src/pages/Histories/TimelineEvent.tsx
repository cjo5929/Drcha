import styles from './Histories.module.scss';

export function TimelineEvent({
  date,
  description,
  icon,
}: {
  date: string;
  description: string;
  icon: string;
}) {
  return (
    <div className={styles.event}>
      <div className={styles.date}>{date}</div>
      <div className={styles.content}>
        <div className={styles.icon}>{icon}</div>
        <div className={styles.description}>{description}</div>
      </div>
    </div>
  );
}
