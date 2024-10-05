import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

import styles from './Layout.module.scss';

export function Layout() {
  const navigate = useNavigate();

  useEffect(() => {
    // TODO : 로그인 상태 확인
    const isLogin = true;
    if (!isLogin) {
      navigate('/login');
    }
  });

  return (
    <div className={styles.container}>
      <Outlet />
    </div>
  );
}
