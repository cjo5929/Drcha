import ShieldSVG from '@/assets/icons/shield.svg?react';

import styles from './TransactionDetail.module.scss';

export function TransactionTitle() {
  return (
    <div className={styles.title}>
      <div className={styles.state}>연체중</div>
      <div className={styles.name}>지종권님께 빌려준</div>
      <div className={styles.amount}>10348134원</div>
      <div className={styles.titlebuttons}>
        <div className={styles.shield}>
          <ShieldSVG />
          상호 동의 기록
        </div>
        <button>차용증 확인</button>
      </div>
    </div>
  );
}
