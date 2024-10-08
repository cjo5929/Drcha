import { useCallback, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';

import styles from './Auth.module.scss';

export function Account() {
  const { chatRoomId } = useParams();
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
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="계좌번호"
          defaultValue={userInfo ? userInfo.accountNo : ''}
          className={styles.input}
        />
      </form>
    </div>
  );
}
