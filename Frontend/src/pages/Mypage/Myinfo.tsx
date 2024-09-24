import { useEffect, useState } from 'react';

import defaultProfile from '@/assets/icons/defaultProfile.png';
import styles from '@/pages/Mypage/Mypage.module.scss';

import { Certificated } from './Certificated';
import { NonCertificated } from './NonCertificated';

export function Myinfo() {
  // 유저 정보 받아오고 isCertificated 설정하기
  useEffect(() => {}, []);

  const [isCertificated] = useState(false);

  return (
    <div className={styles.myinfo}>
      <div className={styles.kakaoinfo}>
        <div className={styles.kakaoprofileimg}>
          <img src={defaultProfile} alt="기본 프로필 이미지" />
        </div>
        <div className={styles.kakaoprofiledetail}>
          <div className={styles.kakaonickname}>OOO님</div>
          <div className={styles.kakaoemail}>asdf1234@naver.com</div>
        </div>
      </div>
      <div className={styles.accountinfo}>
        {/* 인증하기 전, 인증한 후 따로 만들어야 함 */}
        {isCertificated ? <NonCertificated /> : <Certificated />}
      </div>
      <div className={styles.accountbtn}>
        <button>계좌이용내역</button>
        <button>대표계좌변경</button>
      </div>
    </div>
  );
}
