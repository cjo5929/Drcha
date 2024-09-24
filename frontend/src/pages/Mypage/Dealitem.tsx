import styles from '@/pages/Mypage/Mypage.module.scss';
import { TransactionHistory } from '@/types/history';

export function Dealitem({ item }: { item: TransactionHistory }) {
  return (
    <div className={styles.dealItem}>
      {/* 내역 정보를 여기에 출력 */}
      <div>
        {item.cost}
        {item.dates}
        {item.name}
      </div>
    </div>
  );
}
