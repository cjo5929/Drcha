import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import styles from './Auth.module.scss';

const user = { userName: '김박사', accountNo: 1234567890, isVerified: false };

export function Number() {
  const navigate = useNavigate();
  const { state } = useLocation();
  // TODO : 사용자 정보 불러오기
  const { isVerified } = user;

  useEffect(() => {
    if (isVerified) {
      navigate('/', { replace: true });
    }
  }, [navigate, isVerified]);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    navigate('/auth/phone-number', { state: { accountNo: state.accountNo } });
  };

  return (
    <div className={styles.content}>
      <div>
        <span className={styles.blue}>차용박사 뒤 숫자 4자리</span>
        <span>를</span>
        <div>입력해주세요</div>
      </div>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="인증번호"
          className={styles.input}
          defaultValue={state.certificationNumber}
        />
      </form>
    </div>
  );
}
