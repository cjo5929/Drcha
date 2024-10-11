import CertificatedSVG from '@/assets/icons/certificated.svg?react';
import styles from '@/pages/Mypage/Mypage.module.scss';
import { Info } from '@/types/Member';

export function Certificated({ myInfos }: { myInfos: Info }) {
  const formatCurrency = (amount: number) =>
    `${amount.toLocaleString('ko-KR', {
      maximumFractionDigits: 0,
    })}원`;
  return (
    <div className={styles.card}>
      <div className={styles.bankinfo}>
        <div className={styles.bankinfodetail}>
          <div className={styles.bankname}>싸피은행</div>
          <div className={styles.accountnum}>{myInfos.accountNo}</div>
        </div>
        <div className={styles.certokay}>
          <div>
            <CertificatedSVG fill="#ffffff" />
            <span>본인 인증 완료</span>
          </div>
        </div>
      </div>
      <div className={styles.balance}>
        <span>{formatCurrency(myInfos.balance)}</span>
      </div>
    </div>
  );
}
