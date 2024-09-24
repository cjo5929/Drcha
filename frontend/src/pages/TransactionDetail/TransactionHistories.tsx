import GotodetailSVG from '@/assets/icons/gotodetail.svg?react';

import styles from './TransactionDetail.module.scss';

export function TransactionHistories() {
  return (
    <div className={styles.histories}>
      <div className={styles.historyhead}>
        <div className={styles.historyheadtitle}>
          <div className={styles.realtitle}>상세내역</div>
          <div className={styles.lastupdate}>마지막 업데이트 : 12391123</div>
        </div>
        <GotodetailSVG />
      </div>
      {/* 상환일자 */}
      <div className={styles.repayment} />
    </div>
  );
}
