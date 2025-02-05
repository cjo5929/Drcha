/* eslint-disable react/no-danger */
import { RefObject, useCallback, useEffect, useRef, useState } from 'react';

import LogoSVG from '@/assets/icons/logo.svg?react';
import SheildSVG from '@/assets/icons/shield.svg?react';
// import { stamp } from '@/services/stamp';
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
  const formattedStartDate = new Date(startDate.getTime() + 9 * 60 * 60 * 1000);
  const formattedEndDate = new Date(endDate.getTime() + 9 * 60 * 60 * 1000);

  // const [borrowerStamp, setBorrowerStamp] = useState<string | null>(null);
  // const [lenderStamp, setLenderStamp] = useState<string | null>(null);
  const [borrowerStamp, setBorrowerStamp] = useState<boolean>(false);
  const [lenderStamp, setLenderStamp] = useState<boolean>(false);
  const lenderStampRef = useRef<HTMLDivElement>(null);
  const borrowerStampRef = useRef<HTMLDivElement>(null);

  const createStamp = async (
    userType: 'BORROWER' | 'LENDER',
    // userName: string,
  ) => {
    // const stampSVG = await stamp.getStamp(userName);
    if (userType === 'BORROWER') {
      // setBorrowerStamp(stampSVG);
      setBorrowerStamp(true);
      return;
    }
    if (userType === 'LENDER') {
      // setLenderStamp(stampSVG);
      setLenderStamp(true);
    }
  };

  // const resizeStamp = () => {
  //   const lenderStampSvg = lenderStampRef.current?.children[0];
  //   const borrowerStampSvg = borrowerStampRef.current?.children[0];
  //   lenderStampSvg?.setAttribute('viewBox', '25 25 70 70');
  //   borrowerStampSvg?.setAttribute('viewBox', '25 25 70 70');
  // };

  const renderStamp = useCallback(async () => {
    if (iouData.lenderAgreement) {
      // await createStamp('LENDER', iouData.creditorName);
      await createStamp('LENDER');
    }
    if (iouData.borrowerAgreement) {
      // await createStamp('BORROWER', iouData.debtorName);
      await createStamp('BORROWER');
    }
    // resizeStamp();
  }, [iouData]);

  useEffect(() => {
    renderStamp();
  }, [renderStamp]);

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
      <div
        className={`${styles.content} ${type === 'chat' && styles.agreeContent}`}
      >
        <div className={styles.detail}>
          <div className={styles.bold}>차용 일자</div>
          <div>{formattedStartDate.toLocaleString('ko-KR')}</div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>변제 일자</div>
          <div>{formattedEndDate.toLocaleString('ko-KR')}</div>
        </div>
      </div>
      <div
        className={`${styles.content} ${type === 'chat' && styles.agreeContent}`}
      >
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
      <div
        className={`${styles.content} ${type === 'chat' && styles.agreeContent}`}
      >
        <div className={styles.detail}>
          <div className={styles.bold}>채권자</div>
          <div className={styles.userDetail}>
            {iouData.creditorName}
            {iouData.creditorPhoneNumber && (
              <span>({iouData.creditorPhoneNumber})</span>
            )}
            {lenderStamp && (
              <div
                ref={lenderStampRef}
                // dangerouslySetInnerHTML={{ __html: lenderStamp }}
                className={`${styles.stamp} ${type === 'chat' && styles.chatStamp}`}
              >
                <LogoSVG />
              </div>
            )}
          </div>
        </div>
        <div className={styles.detail}>
          <div className={styles.bold}>채무자</div>
          <div className={styles.userDetail}>
            {iouData.debtorName}
            {iouData.debtorPhoneNumber && (
              <span>({iouData.debtorPhoneNumber})</span>
            )}
            {borrowerStamp && (
              <div
                ref={borrowerStampRef}
                // dangerouslySetInnerHTML={{ __html: borrowerStamp }}
                className={`${styles.stamp} ${type === 'chat' && styles.chatStamp}`}
              >
                <LogoSVG />
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
