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
      <div className={styles.repayment}>
        <div className={styles.repaydate}>
          <div className={styles.repaydatetitle}>상환일</div>
          <div className={styles.repaydatedetail}>2024.08.30 (D+5)</div>
        </div>
        <div className={styles.repaydetail}>
          <div className={styles.principal}>
            <div>원금</div>
            <div>510,000원</div>
          </div>
          <div className={styles.interest}>
            <div>이자</div>
            <div>0원 (연 10%)</div>
          </div>
          <div className={styles.total}>
            <div>합계</div>
            <div>510,000원</div>
          </div>
        </div>
        <div className={styles.repayuser}>
          <div>빌려간 사람</div>
          <div>
            <div>지종권</div>
            <div>(010-2341-1234)</div>
          </div>
        </div>
      </div>
    </div>
  );
}
