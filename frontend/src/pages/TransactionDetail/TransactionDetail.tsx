import { Header } from './Header';
import { TransactionAlarm } from './TransactionAlarm';
import styles from './TransactionDetail.module.scss';
import { TransactionHistories } from './TransactionHistories';
import { TransactionTitle } from './TransactionTitle';

export function TransactionDetail() {
  return (
    <div className={styles.container}>
      <Header />
      <TransactionTitle />
      <TransactionAlarm />
      <TransactionHistories />
    </div>
  );
}
