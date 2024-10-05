import styles from '@/pages/Mypage/Mypage.module.scss';
import { MyInfo } from '@/types/MyInfo';

import { Certificated } from './Certificated';
import { NonCertificated } from './NonCertificated';

export function Myinfo({ myInfos }: { myInfos: MyInfo }) {
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
      </div>
      <div className={styles.accountinfo}>
        {myInfos.verified ? <NonCertificated /> : <Certificated />}
      </div>
      <div className={styles.accountbtn}>
        <button>계좌이용내역</button>
        <button>대표계좌변경</button>
      </div>
    </div>
  );
}
