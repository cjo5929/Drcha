import { useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';
import { member } from '@/services/member';
import { autoHyphen } from '@/utils/autoHypen';

import styles from './Auth.module.scss';

export function PhoneNumberPage() {
  const queryParams = new URLSearchParams(window.location.search);
  const chatRoomId = queryParams.get('chatRoomId');
  const navigate = useNavigate();
  const { userInfo } = useUserState();
  const [phoneNumber, setPhoneNumber] = useState('');
  const [isInvalid, setIsInvalid] = useState(false);

  useEffect(() => {
    if (!userInfo) {
      return;
    }
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo]);

  const handleSubmit = useCallback(
    async (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      if (phoneNumber.length === 0 || isInvalid) {
        return;
      }
      await member.registPhoneNumber({ phoneNumber: autoHyphen(phoneNumber) });
      if (chatRoomId) {
        navigate(`/auth/complete?chatRoomId=${chatRoomId}`);
        return;
      }
      navigate('/auth/complete');
    },
    [chatRoomId, navigate, phoneNumber, isInvalid],
  );

  const handlePhoneNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const inputPhoneNumber = e.target.value.replace(/[^0-9]/g, '');
    if (inputPhoneNumber.length > 9) {
      setIsInvalid(false);
      if (inputPhoneNumber.length > 11) {
        return;
      }
    }
    if (inputPhoneNumber.length > 0 && inputPhoneNumber.length < 10) {
      setIsInvalid(true);
    }
    setPhoneNumber(inputPhoneNumber);
  };

  return (
    <div className={styles.content}>
      <div>
        <span className={styles.blue}>전화번호</span>
        <span>를 입력해주세요</span>
      </div>
      <form onSubmit={handleSubmit}>
        <input
          type="tel"
          placeholder="숫자만 입력해주세요"
          value={autoHyphen(phoneNumber)}
          className={`${styles.input} ${isInvalid && styles.invalid}`}
          onInput={handlePhoneNumberChange}
        />
        {isInvalid && (
          <div className={styles.alert}>
            휴대폰 번호를 올바르게 입력해주세요
          </div>
        )}
      </form>
    </div>
  );
}
