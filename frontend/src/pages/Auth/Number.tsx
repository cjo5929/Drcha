import { useCallback, useEffect, useRef, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import { Toast } from '@/components/Toast/Toast';
import { useUserState } from '@/hooks/useUserState';
import { account } from '@/services/account';

import styles from './Auth.module.scss';

export function Number() {
  const queryParams = new URLSearchParams(window.location.search);
  const chatRoomId = queryParams.get('chatRoomId');
  const navigate = useNavigate();
  const { state } = useLocation();
  const { userInfo } = useUserState();
  const [verificationCode, setVerificationCode] = useState<string>();
  const [showToast, setShowToast] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (!userInfo) {
      return;
    }
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo]);

  useEffect(() => {
    if (state.verificationCode) {
      setShowToast(true);

      const hideToastTimer = setTimeout(() => {
        setShowToast(false);
      }, 3000);

      const fillInputTimer = setTimeout(() => {
        if (inputRef.current) {
          inputRef.current.value = state.verificationCode.code;
        }
        setVerificationCode(state.verificationCode.code);
      }, 500);

      return () => {
        clearTimeout(hideToastTimer);
        clearTimeout(fillInputTimer);
      };
    }
  }, [state]);

  const handleSubmit = useCallback(
    async (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      if (verificationCode) {
        const isCheck = await account.checkVerificationCode(verificationCode);
        if (isCheck) {
          if (chatRoomId) {
            navigate(`/auth/phone-number?chatRoomId=${chatRoomId}`);
            return;
          }
          navigate('/auth/phone-number');
        }
      }
    },
    [navigate, chatRoomId, verificationCode],
  );

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
          ref={inputRef}
          type="text"
          placeholder="인증번호"
          className={styles.input}
          onChange={(e) => setVerificationCode(e.target.value)}
        />
      </form>
    </div>
  );
}
