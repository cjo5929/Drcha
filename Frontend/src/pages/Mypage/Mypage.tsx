import styles from '@/pages/Mypage/Mypage.module.scss';

import { MyDealList } from './MyDealList';
import { Myinfo } from './Myinfo';

export function Mypage() {
  return (
    <div className={styles.container}>
      <Myinfo />
      <div className={styles.sumofmoney}>
        <div className={styles.moneydetail}>
          <div>
            빌려준 돈거래
            <span className={styles.bluespan}> 3건</span>
          </div>
          <div>
            <span className={styles.bluespan}>20413원</span>
          </div>
        </div>
        <div className={styles.moneydetail}>
          <div>
            빌린 돈거래
            <span className={styles.redspan}> 3건</span>
          </div>
          <div>
            <span className={styles.redspan}>1023원</span>
          </div>
        </div>
      </div>
      <MyDealList />
    </div>
  );
}
