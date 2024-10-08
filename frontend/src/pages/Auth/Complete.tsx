import { useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import CompleteSVG from '@/assets/icons/complete.svg?react';
import { Button } from '@/components/Button/Button';
import { useUserState } from '@/hooks/useUserState';

import styles from './Auth.module.scss';

export function Complete() {
  const queryParams = new URLSearchParams(window.location.search);
  const chatRoomId = queryParams.get('chatRoomId');
  const navigate = useNavigate();
  const { userInfo, getMyInfo } = useUserState();

  useEffect(() => {
    if (!userInfo) {
      return;
    }
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo]);

  const handleClick = useCallback(async () => {
    await getMyInfo();
    if (chatRoomId) {
      navigate(`/chat/${chatRoomId}`);
      return;
    }
    navigate('/');
  }, [getMyInfo, navigate, chatRoomId]);

  return (
    <div className={`${styles.content} ${styles.complete}`}>
      <div className={styles.complete}>
        <div>본인 인증 완료</div>
        <CompleteSVG />
      </div>
      <Button color="blue" size="long" onClick={handleClick}>
        차용증 생성하러 가기
      </Button>
    </div>
  );
}
