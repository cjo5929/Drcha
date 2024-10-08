import { useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';
import { member } from '@/services/member';
import { PhoneNumber } from '@/types/Member';

import styles from './Auth.module.scss';

export function PhoneNumberPage() {
  const queryParams = new URLSearchParams(window.location.search);
  const chatRoomId = queryParams.get('chatRoomId');
  const navigate = useNavigate();
  const { userInfo } = useUserState();
  const [phoneNumber, setPhoneNumber] = useState<PhoneNumber>({
    phoneNumber: '',
  });

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
      await member.registPhoneNumber(phoneNumber);
      if (chatRoomId) {
        navigate(`/auth/complete?chatRoomId=${chatRoomId}`);
        return;
      }
      navigate('/auth/complete');
    },
    [chatRoomId, navigate, phoneNumber],
  );

  const handlePhoneNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPhoneNumber({ phoneNumber: e.target.value });
  };

  return (
    <div className={`${styles.content}`}>
      <div>
        <span className={styles.blue}>전화번호</span>
        <span>를 입력해주세요</span>
      </div>
      <form onSubmit={handleSubmit}>
        <input
          type="tel"
          placeholder="전화번호"
          className={styles.input}
          value={phoneNumber.phoneNumber}
          onChange={handlePhoneNumberChange}
        />
      </form>
    </div>
  );
}
