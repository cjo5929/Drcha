import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';
import { account } from '@/services/account';

import styles from './Auth.module.scss';

export function Number() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const { userInfo } = useUserState();
  const [verificationCode, setVerificationCode] = useState(
    state?.verificationCode || '',
  );

  useEffect(() => {
    if (!userInfo) {
      return;
    }
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo]);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const isCheck = await account.checkVerificationCode(verificationCode.code);
    if (isCheck) {
      navigate('/auth/phone-number');
    }
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
          value={verificationCode.code}
          onChange={(e) => setVerificationCode(e.target.value)}
        />
      </form>
    </div>
  );
}
