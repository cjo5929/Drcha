import WarningSVG from '@/assets/icons/warning.svg?react';
import { Button } from '@/components/Button/Button';
import styles from '@/pages/Mypage/Mypage.module.scss';

export function NonCertificated() {
  return (
    <div className={styles.cardnoncert}>
      <div>
        <WarningSVG />
        <span>등록된 계좌가 없습니다.</span>
      </div>
      <div>
        <Button color="lightblue" size="small">
          등록하기
        </Button>
      </div>
    </div>
  );
}
