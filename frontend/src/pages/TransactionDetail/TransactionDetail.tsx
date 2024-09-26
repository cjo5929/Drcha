import { Navbar } from '@/components/Navbar/Navbar';

import { Header } from './Header';
import { TransactionAlarm } from './TransactionAlarm';
import styles from './TransactionDetail.module.scss';
import { TransactionGraph } from './TransactionGraph';
import { TransactionHistories } from './TransactionHistories';
import { TransactionTitle } from './TransactionTitle';

export function TransactionDetail() {
  return (
    <div className={styles.container}>
      <Header />
      <TransactionTitle />
      <TransactionAlarm />
      <TransactionHistories />
      <TransactionGraph />
      <Navbar />
    </div>
  );
}
