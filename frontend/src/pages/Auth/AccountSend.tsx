import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import COIN_GIF from '@/assets/images/coin.gif';
import { Button } from '@/components/Button/Button';
import { useUserState } from '@/hooks/useUserState';
import { account } from '@/services/account';
import { VerificationCode } from '@/types/Account';

import styles from './Auth.module.scss';

export function AccountSend() {
  const navigate = useNavigate();
  const { userInfo } = useUserState();

  useEffect(() => {
    if (userInfo.verified) {
      navigate('/', { replace: true });
    }
  }, [navigate, userInfo]);

  const handleClick = async () => {
    const verificationCode: VerificationCode =
      await account.sendVerificationCode();
    navigate('/auth/number', {
      state: { verificationCode },
    });
  };

  return (
    <div className={`${styles.content} ${styles.accountAuthContent}`}>
      <div>
        <img src={COIN_GIF} alt="coin" className={styles.coin} />
        <div>
          <span className={styles.blue}>{userInfo.username}</span>
          <span>님 계좌가 맞는지</span>
          <div>확인하기 위해</div>
          <div>
            <span>차용박사가 </span>
            <span className={styles.blue}>1원</span>
            <span>을 보내볼게요</span>
          </div>
        </div>
      </div>
      <Button color="blue" size="long" onClick={handleClick}>
        내 계좌로 1원 보내기
      </Button>
    </div>
  );
}
