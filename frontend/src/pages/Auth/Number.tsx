import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import { Toast } from '@/components/Toast/Toast';
import { useUserState } from '@/hooks/useUserState';
import { account } from '@/services/account';

import styles from './Auth.module.scss';

export function Number() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const { userInfo } = useUserState();
  const [verificationCode, setVerificationCode] = useState<string>();
  const [showToast, setShowToast] = useState(false);

  useEffect(() => {
    if (!userInfo) {
      return;
    }
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo]);

  useEffect(() => {
    if (state?.verificationCode) {
      setShowToast(true);

      const hideToastTimer = setTimeout(() => {
        setShowToast(false);
      }, 3000);

      const fillInputTimer = setTimeout(() => {
        setVerificationCode(state.verificationCode.code);
      }, 3700);

      return () => {
        clearTimeout(hideToastTimer);
        clearTimeout(fillInputTimer);
      };
    }
  }, [state]);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (verificationCode) {
      const isCheck = await account.checkVerificationCode(verificationCode);
      if (isCheck) {
        navigate('/auth/phone-number');
      }
    }
  };

  return (
    <div className={styles.content}>
      <Toast
        code={state?.verificationCode.code}
        accountNo={state?.verificationCode.accountNo}
        isVisible={showToast}
      />
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
          value={verificationCode}
          onChange={(e) => setVerificationCode(e.target.value)}
        />
      </form>
    </div>
  );
}
