import { useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';

import styles from './Auth.module.scss';
import { Button } from '../../components/Button/Button';

export function Account() {
  const queryParams = new URLSearchParams(window.location.search);
  const chatRoomId = queryParams.get('chatRoomId');
  const navigate = useNavigate();
  const { userInfo } = useUserState();

  useEffect(() => {
    if (!userInfo) {
      return;
    }
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo, chatRoomId]);

  const handleSubmit = useCallback(
    (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      if (chatRoomId) {
        navigate(`/auth/account-send?chatRoomId=${chatRoomId}`);
        return;
      }
      navigate('/auth/account-send');
    },
    [navigate, chatRoomId],
  );

  return (
    <div className={styles.content}>
      <div>
        <span>거래를 도와드릴게요, </span>
        <span className={styles.blue}>{userInfo ? userInfo.username : ''}</span>
        <span>님</span>
      </div>
      <div>
        <div>계좌 인증을 진행할 수 있도록</div>
        <div>
          <span className={styles.blue}>계좌번호</span>
          <span>를 입력해주세요</span>
        </div>
      </div>
      <form onSubmit={handleSubmit} className={styles.form}>
        <input
          type="text"
          placeholder="계좌번호"
          defaultValue={userInfo ? userInfo.accountNo : ''}
          className={styles.input}
        />
        <Button type="submit" color="lightblue" size="small">
          다음
        </Button>
      </form>
    </div>
  );
}
