import { useNavigate } from 'react-router-dom';

import ChaticonSVG from '@/assets/icons/chaticon.svg?react';
import MypageiconSVG from '@/assets/icons/mypageicon.svg?react';
import StarticonSVG from '@/assets/icons/starticon.svg?react';

import styles from './Navbar.module.scss';

export function Navbar() {
  const navigate = useNavigate();
  return (
    <div className={styles.navbar}>
      <div
        className={styles.navItem}
        onClick={() => {
          navigate('/');
        }}
      >
        <ChaticonSVG />
        채팅
      </div>
      <div className={styles.navItem}>
        <StarticonSVG />
        거래 시작
      </div>
      <div
        className={styles.navItem}
        onClick={() => {
          navigate('/mypage');
        }}
      >
        <MypageiconSVG />
        마이페이지
      </div>
    </div>
  );
}
