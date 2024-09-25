import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import COIN_GIF from '@/assets/images/coin.gif';
import { Button } from '@/components/Button/Button';

import styles from './Auth.module.scss';

const user = { userName: '김박사', accountNo: 1234567890, isVerified: false };

export function AccountSend() {
  const navigate = useNavigate();
  const { state } = useLocation();
  // TODO : 사용자 정보 불러오기
  const { isVerified } = user;
  // TODO : 1원 인증번호 불러오기
  const certificationNumber = 1234;

  useEffect(() => {
    if (isVerified) {
      navigate('/', { replace: true });
    }
  }, [navigate, isVerified]);

  const handleClick = () => {
    navigate('/auth/number', {
      state: { accountNo: state.accountNo, certificationNumber },
    });
  };

  return (
    <div className={`${styles.content} ${styles.accountAuthContent}`}>
      <div>
        <img src={COIN_GIF} alt="coin" className={styles.coin} />
        <div>
          <span className={styles.blue}>{state.userName}</span>
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
