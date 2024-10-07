import { useEffect, useState } from 'react';

import { IouDetailData } from '@/types/iou';

import styles from './TransactionDetail.module.scss';

export function TransactionGraph({
  types,
  curiou,
}: {
  types: string;
  curiou: IouDetailData;
}) {
  const [paidPercentage, setPaidPercentage] = useState<number>(0);

  useEffect(() => {
    try {
      if (!curiou) {
        throw new Error('IOU is missing');
      }
      setPaidPercentage(
        Math.round(
          ((curiou.totalAmount - curiou.iouBalance) / curiou.totalAmount) * 100,
        ),
      );
    } catch (err) {
      throw new Error('something is missing');
    }
  }, [types, curiou]);

  const formatCurrency = (amount: number) =>
    `${amount.toLocaleString('ko-KR', {
      maximumFractionDigits: 0,
    })}원`;

  return (
    <div className={styles.graph}>
      <div className={styles.graphtitle}>상환 현황</div>

      {/* 상환된 금액과 퍼센트 차트 */}
      <div className={styles.chartContainer}>
        <div className={styles.chart}>
          <div className={styles.bar} style={{ width: `${paidPercentage}%` }} />
        </div>
        <div className={styles.percentage}>상환율 : {paidPercentage}%</div>
      </div>

      {/* 금액 정보 */}
      <div className={styles.amountInfo}>
        <div>
          <div>현재 상환 금액</div>
          <div>{formatCurrency(curiou.totalAmount - curiou.iouBalance)}</div>
        </div>
        <div>
          <div>잔여 상환 금액(이자 포함)</div>
          <div>{formatCurrency(curiou.iouBalance)}</div>
        </div>
      </div>

      {/* 설명 부분 */}
      <div className={styles.description}>
        본 상환 현황은 채권자(또는 동의 요청자)에 의해 기록되는 정보로 참고
        용도입니다.
      </div>
    </div>
  );
}
