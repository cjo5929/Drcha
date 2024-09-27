import HamburgerSVG from '@/assets/icons/hamburger.svg?react';
import LeftarrowSVG from '@/assets/icons/leftArrow.svg?react';

import styles from './Chat.module.scss';

export function Header() {
  return (
    <div className={styles.header}>
      <LeftarrowSVG fill="blue" />
      <div className={styles.userinfo}>조현수</div>
      <HamburgerSVG />
    </div>
  );
}
