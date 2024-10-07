import { useNavigate } from 'react-router-dom';

import CompleteSVG from '@/assets/icons/complete.svg?react';
import styles from '@/pages/Mypage/Mypage.module.scss';
import { TransactionHistory } from '@/types/history';

export function Dealitem({
  item,
  lendorborrow,
}: {
  item: TransactionHistory;
  lendorborrow: string;
}) {
  const navigate = useNavigate();
  const handleDetail = () => {
    navigate(`detail/${lendorborrow}/${item.iouId}`);
  };

  const formatDate = (dateString: string) => {
    const kstDate = new Date(dateString);
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
  return (
    <div className={styles.dealItem} onClick={handleDetail}>
      {/* 내역 정보를 여기에 출력 */}
      <div className={styles.dealItemOne}>
        <div>{item.opponentName}</div>
        <div>{formatCurrency(item.principalAmount)}</div>
      </div>
      <div className={styles.dealItemTwo}>
        <div className={styles.checked}>
          <div>
            <CompleteSVG width="12" height="12" />
          </div>
          &nbsp;&nbsp;
          <div>동의함</div>
        </div>
        <div>{formatDate(item.contractStartDate)}</div>
      </div>
    </div>
  );
}
