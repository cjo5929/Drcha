import styles from './TransactionDetail.module.scss';

export function TransactionTitle() {
  return (
    <div className={styles.title}>
      <div className={styles.state}>연체중</div>
      <div className={styles.name}>지종권님께 빌려준</div>
      <div className={styles.amount} />
    </div>
  );
}
