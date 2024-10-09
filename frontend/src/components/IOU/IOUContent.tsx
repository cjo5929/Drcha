import { RefObject } from 'react';

import SheildSVG from '@/assets/icons/shield.svg?react';
import { IouData } from '@/types/iou';

import styles from './IOU.module.scss';

type IOUContentProps = {
  iouData: IouData;
  iouRef?: RefObject<HTMLDivElement>;
  type: 'page' | 'chat';
};

export function IOUContent({ iouData, iouRef, type }: IOUContentProps) {
  const { iouAmount, interestRate, contractStartDate, contractEndDate } =
    iouData;
  const interestAmount = Math.round(iouAmount * (interestRate / 100));
  const startDate = new Date(contractStartDate);
  const endDate = new Date(contractEndDate);
  // KST로 변경
  startDate.setHours(startDate.getHours() + 9);
  endDate.setHours(endDate.getHours() + 9);

  return (
    <div className={styles.iou} ref={iouRef}>
      <div
        className={`${type === 'page' && styles.title} ${type === 'chat' && styles.chatTitle}`}
      >
        금 전 차 용 증 서
      </div>
      <div className={styles.description}>
        <div>
          <span className={styles.bold}>{iouData.debtorName}</span>(이하
          &apos;채무자&apos;라고 합니다)는(은)
        </div>
        <div>
          <span className={styles.bold}>{iouData.creditorName}</span>(이하
          &apos;채권자&apos;라고 합니다)(으)로부터
        </div>
        <div>아래와 같이 차용하였음을 확인하고,</div>
        <div>변제할 것을 확약합니다.</div>
      </div>
      <div className={styles.content}>
        <div className={styles.detail}>
          <div className={styles.bold}>차용 일자</div>
          <div>{startDate.toLocaleString('ko-KR')}</div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>변제 일자</div>
          <div>{endDate.toLocaleString('ko-KR')}</div>
        </div>
      </div>
      <div className={styles.content}>
        <div className={styles.detail}>
          <div className={styles.bold}>원금</div>
          <div>{iouData.iouAmount.toLocaleString('ko-KR')}원</div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>이자 금액</div>
          <div>
            {interestAmount.toLocaleString('ko-KR')}원(연 {iouData.interestRate}
            %)
          </div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>원리금 합산</div>
          <div>{iouData.totalAmount.toLocaleString('ko-KR')}원</div>
        </div>
      </div>
      <div className={styles.agreement}>
        <SheildSVG />
        상호 동의 기록
      </div>
      <div className={styles.content}>
        <div className={styles.detail}>
          <div className={styles.bold}>채권자</div>
          <div>
            {iouData.creditorName}
            {iouData.creditorPhoneNumber && (
              <span>({iouData.creditorPhoneNumber})</span>
            )}
          </div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>채무자</div>
          <div>
            {iouData.debtorName}
            {iouData.debtorPhoneNumber && (
              <span>({iouData.debtorPhoneNumber})</span>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
