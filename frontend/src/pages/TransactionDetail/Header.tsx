import { useNavigate } from 'react-router-dom';

import BackiconSVG from '@/assets/icons/backicon.svg?react';

import styles from './TransactionDetail.module.scss';

export function Header() {
  const navigate = useNavigate();
  return (
    <div className={styles.header}>
      <BackiconSVG
        onClick={() => {
          navigate('/mypage');
        }}
      />
    </div>
  );
}
