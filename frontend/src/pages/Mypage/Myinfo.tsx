import { useNavigate } from 'react-router-dom';

import styles from '@/pages/Mypage/Mypage.module.scss';
import { member } from '@/services/member';
import { Info } from '@/types/Member';

import { Certificated } from './Certificated';
import { NonCertificated } from './NonCertificated';

export function Myinfo({ myInfos }: { myInfos: Info }) {
  const navigate = useNavigate();
  const handleLogout = () => {
    member.doLogout();
    navigate('/');
  };
  return (
    <div className={styles.myinfo}>
      <div className={styles.kakaoinfo}>
        <div className={styles.kakaoprofileimg}>
          <img src={myInfos.avatarUrl} alt="기본 프로필 이미지" />
        </div>
        <div className={styles.kakaoprofiledetail}>
          <div className={styles.kakaonickname}>{myInfos.username}님</div>
          <div className={styles.kakaoemail}>{myInfos.email}</div>
        </div>
        <button className={styles.logout} onClick={handleLogout}>
          로그아웃
        </button>
      </div>
      <div className={styles.accountinfo}>
        {myInfos.verified ? (
          <Certificated myInfos={myInfos} />
        ) : (
          <NonCertificated />
        )}
      </div>
    </div>
  );
}
