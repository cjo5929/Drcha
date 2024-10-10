import CopyToClipboard from 'react-copy-to-clipboard';
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
  curhistory: TransactionDetailHistory;
}) {
  const navigate = useNavigate();
  const handleDetail = () => {
    navigate('/histories', { state: { curhistory } });
  };

  const addKSTOffset = (date: Date): Date =>
    new Date(date.getTime() + 9 * 60 * 60 * 1000);

  const getLastUpdateDate = () => {
    if (curhistory.transactions.length === 0) {
      return '업데이트 없음';
    }
    const lastTransaction =
      curhistory.transactions[curhistory.transactions.length - 1];
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

  const formatDate = (dateString: string) => {
    const kstDate = addKSTOffset(new Date(dateString));
    return kstDate.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      timeZone: 'Asia/Seoul',
    });
  };

  const formatCurrency = (amount: number) =>
    `${amount.toLocaleString('ko-KR', {
      maximumFractionDigits: 0,
    })}원`;

  const formatDDay = (daysUntilDue: number) => {
    if (daysUntilDue === 0) {
      return 'D-Day';
    }
    const prefix = daysUntilDue > 0 ? 'D-' : 'D+';
    return `${prefix}${Math.abs(daysUntilDue)}`;
  };

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
            {formatDate(curiou.contractStartDate)} (
            {formatDDay(curiou.daysUntilDue)})
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
        {types !== 'lend' && (
          <div className={styles.repayuser}>
            <div>갚을 계좌</div>
            <div>{curiou.virtualAccountNumber}</div>
            <CopyToClipboard text={curiou.virtualAccountNumber}>
              <button
                style={{
                  color: '#808080',
                  width: '24px',
                  height: '24px',
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  borderRadius: '4px',
                }}
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth="1.5"
                  stroke="currentColor"
                  style={{ width: '24px', height: '24px' }} // 크기 명시
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M15.75 17.25v3.375c0 .621-.504 1.125-1.125 1.125h-9.75a1.125 1.125 0 0 1-1.125-1.125V7.875c0-.621.504-1.125 1.125-1.125H6.75a9.06 9.06 0 0 1 1.5.124m7.5 10.376h3.375c.621 0 1.125-.504 1.125-1.125V11.25c0-4.46-3.243-8.161-7.5-8.876a9.06 9.06 0 0 0-1.5-.124H9.375c-.621 0-1.125.504-1.125 1.125v3.5m7.5 10.375H9.375a1.125 1.125 0 0 1-1.125-1.125v-9.25m12 6.625v-1.875a3.375 3.375 0 0 0-3.375-3.375h-1.5a1.125 1.125 0 0 1-1.125-1.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H9.75"
                  />
                </svg>
              </button>
            </CopyToClipboard>
          </div>
        )}
      </div>
    </div>
  );
}
