import { Navbar } from '@/components/Navbar/Navbar';

import { Header } from './Header';
import styles from './Histories.module.scss';
import { HistoriesDetail } from './HistoriesDetail';

export function Histories() {
  return (
    <div className={styles.container}>
      <Header />
      <div className={styles.titlecontainer}>
        <div className={styles.title}>상세 내역</div>
        <div className={styles.lastupdate}>마지막 업데이트 : 1231413</div>
      </div>
      <HistoriesDetail />
      <Navbar />
    </div>
  );
}
