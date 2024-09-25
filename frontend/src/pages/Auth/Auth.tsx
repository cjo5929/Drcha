import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import { BackwardButton } from '@/components/BackwardButton/BackwardButton';

import styles from './Auth.module.scss';

const user = { userName: '김박사', accountNo: 1234567890, isVerified: false };

export function Auth() {
  const navigate = useNavigate();
  // TODO : 사용자 정보 불러오기
  const { isVerified } = user;

  useEffect(() => {
    if (isVerified) {
      navigate('/', { replace: true });
    }
  }, [navigate, isVerified]);

  return (
    <div>
      <div className={styles.backwardButton}>
        <BackwardButton />
      </div>
      <Outlet />
    </div>
  );
}
