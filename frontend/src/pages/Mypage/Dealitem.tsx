import { useNavigate } from 'react-router-dom';

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
  return (
    <div className={styles.dealItem} onClick={handleDetail}>
      {/* 내역 정보를 여기에 출력 */}
      <div>
        {item.principalAmount}
        {item.contractStartDate}
        {item.opponentName}
      </div>
    </div>
  );
}
