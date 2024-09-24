import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import CompleteSVG from '@/assets/icons/complete.svg?react';
import { Button } from '@/components/Button/Button';

import styles from './Auth.module.scss';

const user = { userName: '김박사', accountNo: 1234567890, isVerified: false };

export function Complete() {
  const navigate = useNavigate();
  // TODO : 사용자 정보 불러오기
  const { isVerified } = user;

  useEffect(() => {
    if (isVerified) {
      navigate('/', { replace: true });
    }
  }, [navigate, isVerified]);

  const handleClick = () => {
    navigate('/');
  };

  return (
    <div className={`${styles.content} ${styles.complete}`}>
      <div className={styles.complete}>
        <div>본인 인증 완료</div>
        <CompleteSVG />
      </div>
      <Button color="blue" size="long" onClick={handleClick}>
        차용증 생성하러 가기
      </Button>
    </div>
  );
}
