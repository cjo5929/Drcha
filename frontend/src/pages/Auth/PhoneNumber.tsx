import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useUserState } from '@/hooks/useUserState';
import { member } from '@/services/member';
import { PhoneNumber, Info } from '@/types/Member';

import styles from './Auth.module.scss';

export function PhoneNumberPage() {
  const navigate = useNavigate();
  const { userInfo } = useUserState() as { userInfo: Info };
  const [phoneNumber, setPhoneNumber] = useState<PhoneNumber>({
    phoneNumber: '',
  });

  useEffect(() => {
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo]);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    await member.registPhoneNumber(phoneNumber);
    navigate('/auth/complete');
  };

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
