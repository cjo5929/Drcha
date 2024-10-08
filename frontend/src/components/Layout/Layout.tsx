import { useEffect } from 'react';
import { Outlet, useNavigate, useParams } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';

import styles from './Layout.module.scss';

export function Layout() {
  const navigate = useNavigate();
  const { isLogin, userInfo } = useUserState();
  const { chatRoomId } = useParams();

  useEffect(() => {
    if (!isLogin) {
      if (chatRoomId) {
        navigate('/login', {
          state: { chatRoomId },
        });
        return;
      }
      navigate('/login');
    }
    if (userInfo && !userInfo?.verified) {
      navigate(`/auth/account?chatRoomId=${chatRoomId}`);
    }
  }, [chatRoomId, isLogin, userInfo, navigate]);

  return (
    <div className={styles.container}>
      <Outlet />
    </div>
  );
}
