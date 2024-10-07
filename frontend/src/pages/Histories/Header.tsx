import { useNavigate } from 'react-router-dom';

import BackiconSVG from '@/assets/icons/backicon.svg?react';

import styles from './Histories.module.scss';

export function Header() {
  const navigate = useNavigate();
  return (
    <div className={styles.header}>
      <BackiconSVG
        onClick={() => {
          navigate(-1);
        }}
      />
    </div>
  );
}
