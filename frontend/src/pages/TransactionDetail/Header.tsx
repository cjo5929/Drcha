import BackiconSVG from '@/assets/icons/backicon.svg?react';
import TrashcanSVG from '@/assets/icons/trashcan.svg?react';

import styles from './TransactionDetail.module.scss';

export function Header() {
  return (
    <div className={styles.header}>
      <BackiconSVG />
      <TrashcanSVG />
    </div>
  );
}
