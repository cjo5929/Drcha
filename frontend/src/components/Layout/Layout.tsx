import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';

import styles from './Layout.module.scss';

export function Layout() {
  const navigate = useNavigate();
  const { isLogin } = useUserState();

  useEffect(() => {
    if (!isLogin) {
      navigate('/login');
    }
  }, [isLogin, navigate]);

  return (
    <div className={styles.container}>
      <Outlet />
    </div>
  );
}
