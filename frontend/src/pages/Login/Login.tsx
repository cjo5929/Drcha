import { useLocation } from 'react-router-dom';

import KakaoSVG from '@/assets/icons/kakao.svg?react';
import LogoSVG from '@/assets/icons/logo.svg?react';
import STAMP_IMAGE from '@/assets/images/stamp.png';
import { URL } from '@/constants/url';

import styles from './Login.module.scss';

export function Login() {
  const { state } = useLocation();

  const handleLogin = () => {
    if (state) {
      if (state.chatRoomId) {
        window.location.href = `${URL.LOGIN}/oauth2/authorization/kakao?custom_state=${state.chatRoomId}`;
        return;
      }
    }
    window.location.href = `${URL.LOGIN}/oauth2/authorization/kakao`;
  };

  return (
    <div className={styles.container}>
      <div className={styles.title}>
        <div className={styles.main}>
          <div>차용</div>
          <div>박사</div>
        </div>
        <LogoSVG className={styles.logo} />
        <img src={STAMP_IMAGE} alt="stamp" className={styles.stamp} />
      </div>
      <button className={styles.loginButton} onClick={handleLogin}>
        <KakaoSVG />
        카카오 로그인
      </button>
    </div>
  );
}
