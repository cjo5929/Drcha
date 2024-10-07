import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import ShieldSVG from '@/assets/icons/shield.svg?react';
import { IouDetailData } from '@/types/iou';

import styles from './TransactionDetail.module.scss';

export function TransactionTitle({
  types,
  curiou,
}: {
  types: string;
  curiou: IouDetailData;
}) {
  const navigate = useNavigate();
  const [curState, setCurState] = useState(' ');

  useEffect(() => {
    if (curiou.contractStatus === 'ACTIVE') {
      setCurState('상환 중');
    } else if (curiou.contractStatus === 'OVERDUE') {
      setCurState('연체 중');
    } else {
      setCurState('상환 완료');
    }
  }, [types, curiou]);

  const formatCurrency = (amount: number) =>
    `${amount.toLocaleString('ko-KR', {
      maximumFractionDigits: 0,
    })}원`;

  const handleIOU = () => {
    navigate(`/iou/${curiou.iouId}`);
  };

  return (
    <div className={styles.title}>
      <div className={styles.state}>{curState}</div>
      <div className={styles.name}>
        {types === 'lend' ? curiou.debtorName : curiou.creditorName}님께{' '}
        {types === 'lend' ? '빌려준' : '빌린'}
      </div>
      <div className={styles.amount}>{formatCurrency(curiou.iouAmount)}</div>
      <div className={styles.titlebuttons}>
        <div className={styles.shield}>
          <ShieldSVG />
          상호 동의 기록
        </div>
        <button onClick={handleIOU}>차용증 확인</button>
      </div>
    </div>
  );
}
