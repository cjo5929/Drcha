import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';

import styles from './Auth.module.scss';

export function Account() {
  const navigate = useNavigate();
  const { userInfo } = useUserState();

  useEffect(() => {
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo]);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    navigate('/auth/account-send');
  };

  return (
    <div className={styles.content}>
      <div>
        <span>거래를 도와드릴게요, </span>
        <span className={styles.blue}>{userInfo.username}</span>
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
          defaultValue={userInfo.accountNo}
          className={styles.input}
        />
      </form>
    </div>
  );
}
