import { useLocation } from 'react-router-dom';

import { Navbar } from '@/components/Navbar/Navbar';
import { TransactionDetailHistory } from '@/types/history';

import { Header } from './Header';
import styles from './Histories.module.scss';
import { HistoriesDetail } from './HistoriesDetail';

export function Histories() {
  const location = useLocation();
  const curhistory = location.state?.curhistory as
    | TransactionDetailHistory[]
    | undefined;
  const addKSTOffset = (date: Date): Date =>
    new Date(date.getTime() + 9 * 60 * 60 * 1000);
  const getLastUpdateDate = () => {
    if (!curhistory || curhistory.length === 0) {
      return '업데이트 없음';
    }
    const lastTransaction = curhistory[curhistory.length - 1];
    const kstDate = addKSTOffset(new Date(lastTransaction.transactionDate));
    return kstDate.toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      timeZone: 'Asia/Seoul',
    });
  };

  return (
    <div className={styles.container}>
      <Header />
      <div className={styles.titlecontainer}>
        <div className={styles.title}>상세 내역</div>
        <div className={styles.lastupdate}>
          마지막 업데이트 : {getLastUpdateDate()}
        </div>
      </div>
      <HistoriesDetail curhistory={curhistory} />
      <Navbar />
    </div>
  );
}
