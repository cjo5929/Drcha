import ChaticonSVG from '@/assets/icons/chaticon.svg?react';
import MypageiconSVG from '@/assets/icons/mypageicon.svg?react';
import StarticonSVG from '@/assets/icons/starticon.svg?react';

import styles from './Navbar.module.scss';

export function Navbar() {
  return (
    <div className={styles.navbar}>
      <div className={styles.navItem}>
        <ChaticonSVG />
        채팅
      </div>
      <div className={styles.navItem}>
        <StarticonSVG />
        거래 시작
      </div>
      <div className={styles.navItem}>
        <MypageiconSVG />
        마이페이지
      </div>
    </div>
  );
}
