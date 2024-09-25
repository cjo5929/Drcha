import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import styles from './Auth.module.scss';

const user = { userName: '김박사', accountNo: 1234567890, isVerified: false };

export function PhoneNumber() {
  const navigate = useNavigate();
  // TODO : 사용자 정보 불러오기
  const { isVerified } = user;

  useEffect(() => {
    if (isVerified) {
      navigate('/', { replace: true });
    }
  }, [navigate, isVerified]);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    navigate('/auth/complete');
  };

  return (
    <div className={`${styles.content}`}>
      <div>
        <span className={styles.blue}>전화번호</span>
        <span>를 입력해주세요</span>
      </div>
      <form onSubmit={handleSubmit}>
        <input type="text" placeholder="전화번호" className={styles.input} />
      </form>
    </div>
  );
}
