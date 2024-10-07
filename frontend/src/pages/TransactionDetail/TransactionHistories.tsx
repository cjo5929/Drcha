import { useNavigate } from 'react-router-dom';

import GotodetailSVG from '@/assets/icons/gotodetail.svg?react';
import { TransactionDetailHistory } from '@/types/history';
import { IouDetailData } from '@/types/iou';

import styles from './TransactionDetail.module.scss';

export function TransactionHistories({
  types,
  curiou,
  curhistory,
}: {
  types: string;
  curiou: IouDetailData;
  curhistory: Array<TransactionDetailHistory>;
}) {
  const navigate = useNavigate();
  const handleDetail = () => {
    navigate('/histories', { state: { curhistory } });
  };
  const getLastUpdateDate = () => {
    if (curhistory.length === 0) {
      return '업데이트 없음';
    }
    const lastTransaction = curhistory[curhistory.length - 1];
    return new Date(lastTransaction.transactionDate).toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const formatDate = (dateString: string) =>
    new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    });

  const formatCurrency = (amount: number) =>
    `${amount.toLocaleString('ko-KR', {
      maximumFractionDigits: 0,
    })}원`;

  return (
    <div className={styles.histories}>
      <div className={styles.historyhead} onClick={handleDetail}>
        <div className={styles.historyheadtitle}>
          <div className={styles.realtitle}>상세내역</div>
          <div className={styles.lastupdate}>
            마지막 업데이트: {getLastUpdateDate()}
          </div>
        </div>
        <GotodetailSVG />
      </div>
      {/* 상환일자 */}
      <div className={styles.repayment}>
        <div className={styles.repaydate}>
          <div className={styles.repaydatetitle}>상환일</div>
          <div className={styles.repaydatedetail}>
            {formatDate(curiou.contractStartDate)} (D
            {curiou.daysUntilDue >= 0 ? '-' : '+'}
            {curiou.daysUntilDue >= 0
              ? curiou.daysUntilDue
              : curiou.daysUntilDue * -1}
            )
          </div>
        </div>
        <div className={styles.repaydetail}>
          <div className={styles.principal}>
            <div>원금</div>
            <div>{formatCurrency(curiou.iouAmount)}</div>
          </div>
          <div className={styles.interest}>
            <div>이자</div>
            <div>
              {formatCurrency(curiou.totalAmount - curiou.iouAmount)} (연{' '}
              {curiou.interestRate}%)
            </div>
          </div>
          <div className={styles.total}>
            <div>합계</div>
            <div>{formatCurrency(curiou.totalAmount)}</div>
          </div>
        </div>
        <div className={styles.repayuser}>
          <div>거래 상대</div>
          <div>
            <div>
              {types === 'lend' ? curiou.debtorName : curiou.creditorName}
            </div>
            <div>({curiou.phoneNumber})</div>
          </div>
        </div>
      </div>
    </div>
  );
}
