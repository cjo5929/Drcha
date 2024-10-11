import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import { BackwardButton } from '@/components/BackwardButton/BackwardButton';
import { useUserState } from '@/hooks/useUserState';

import styles from './Auth.module.scss';

export function Auth() {
  const navigate = useNavigate();
  const { userInfo } = useUserState();

  useEffect(() => {
    if (!userInfo) {
      return;
    }
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo]);

  return (
    <div>
      <div className={styles.backwardButton}>
        <BackwardButton />
      </div>
      <Outlet />
    </div>
  );
}
