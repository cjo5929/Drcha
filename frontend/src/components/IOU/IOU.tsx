import BackiconSVG from '@/assets/icons/backicon.svg?react';
import DownloadSVG from '@/assets/icons/download.svg?react';
import SheildSVG from '@/assets/icons/shield.svg?react';

import styles from './IOU.module.scss';

export function IOU() {
  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <button>
          <BackiconSVG />
        </button>
        <button>
          <DownloadSVG />
        </button>
      </div>
      <div className={styles.title}>금 전 차 용 증 서</div>
      <div className={styles.description}>
        <div>
          <span className={styles.bold}>강민서</span>(이하
          &apos;채무자&apos;라고 합니다)는
        </div>
        <div>
          <span className={styles.bold}>지종권</span>(이하
          &apos;채권자&apos;라고 합니다)로부터
        </div>
        <div>아래와 같이 차용하였음을 확인하고,</div>
        <div>변제할 것을 확약합니다.</div>
      </div>
      <div className={styles.content}>
        <div className={styles.detail}>
          <div className={styles.bold}>차용 일자</div>
          <div>2024.09.30</div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>변제 일자</div>
          <div>2024.11.30</div>
        </div>
      </div>
      <div className={styles.content}>
        <div className={styles.detail}>
          <div className={styles.bold}>원금</div>
          <div>300000원</div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>이자 금액</div>
          <div>30000원(연 10%)</div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>원리금 합산</div>
          <div>330000원</div>
        </div>
      </div>
      <div className={styles.agreement}>
        <SheildSVG />
        상호 동의 기록
      </div>
      <div className={styles.content}>
        <div className={styles.detail}>
          <div className={styles.bold}>채권자</div>
          <div>지종권(010-0000-0000)</div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>채무자</div>
          <div>강민서(010-0000-0000)</div>
        </div>
      </div>
    </div>
  );
}
