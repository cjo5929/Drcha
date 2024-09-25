import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import styles from './Auth.module.scss';

const user = { userName: '김박사', accountNo: 1234567890, isVerified: false };

export function Account() {
  const navigate = useNavigate();
  // TODO : 사용자 정보 불러오기
  const { userName, accountNo, isVerified } = user;

  useEffect(() => {
    if (isVerified) {
      navigate('/', { replace: true });
    }
  }, [navigate, isVerified]);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    navigate('/auth/account-send', { state: { userName, accountNo } });
  };

  return (
    <div className={styles.content}>
      <div>
        <span>거래를 도와드릴게요, </span>
        <span className={styles.blue}>{userName}</span>
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
          defaultValue={accountNo}
          className={styles.input}
        />
      </form>
    </div>
  );
}
