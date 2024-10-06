import { useEffect } from 'react';
import { Outlet, useNavigate, useParams } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';

import styles from './Layout.module.scss';

export function Layout() {
  const navigate = useNavigate();
  const { isLogin } = useUserState();
  const { chatRoomId } = useParams();

  useEffect(() => {
    if (!isLogin) {
      if (chatRoomId) {
        navigate(`/login/${chatRoomId}`);
        return;
      }
      navigate('/login');
    }
  }, [chatRoomId, isLogin, navigate]);

  return (
    <div className={styles.container}>
      <Outlet />
    </div>
  );
}
