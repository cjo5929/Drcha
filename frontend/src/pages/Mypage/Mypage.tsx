import { useEffect, useState } from 'react';

import { Navbar } from '@/components/Navbar/Navbar';
import { useUserState } from '@/hooks/useUserState';
import styles from '@/pages/Mypage/Mypage.module.scss';
import { iou } from '@/services/iou';
import { TransactionHistory } from '@/types/history';
import { Info } from '@/types/Member';

import { MyDealList } from './MyDealList';
import { Myinfo } from './Myinfo';

export function Mypage() {
  const [myData, setMyData] = useState<Info | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { userInfo } = useUserState();

  const [lendItems, setLendItems] = useState<Array<TransactionHistory>>([]);
  const [borrowItems, setBorrowItems] = useState<Array<TransactionHistory>>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        if (userInfo) {
          setMyData(userInfo);
        } else {
          setMyData(null);
        }

        const [lendingData, borrowingData] = await Promise.all([
          iou.getLendingRecords(),
          iou.getBorrowingRecords(),
        ]);

        const transformData = (
          data: Array<TransactionHistory>,
        ): TransactionHistory[] =>
          data.map((item: TransactionHistory) => ({
            iouId: item.iouId,
            opponentName: item.opponentName,
            principalAmount: item.principalAmount,
            contractStartDate: item.contractStartDate,
            agreementStatus: item.agreementStatus,
            contractStatus: item.contractStatus,
          }));

        setLendItems(transformData(lendingData));
        setBorrowItems(transformData(borrowingData));
      } catch (e) {
        setError('Failed to fetch data');
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [userInfo]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  const lendCount = lendItems.length;
  const borrowCount = borrowItems.length;
  const lendTotal = lendItems.reduce(
    (sum, item) => sum + item.principalAmount,
    0,
  );
  const borrowTotal = borrowItems.reduce(
    (sum, item) => sum + item.principalAmount,
    0,
  );

  const formatCurrency = (amount: number) =>
    `${amount.toLocaleString('ko-KR', {
      maximumFractionDigits: 0,
    })}원`;

  return (
    <div className={styles.container}>
      {myData && <Myinfo myInfos={myData} />}
      <div className={styles.sumofmoney}>
        <div className={styles.moneydetail}>
          <div>
            빌려준 돈거래
            <span className={styles.bluespan}> {lendCount}건</span>
          </div>
          <div>
            <span className={styles.bluespan}>{formatCurrency(lendTotal)}</span>
          </div>
        </div>
        <div className={styles.moneydetail}>
          <div>
            빌린 돈거래
            <span className={styles.redspan}> {borrowCount}건</span>
          </div>
          <div>
            <span className={styles.redspan}>
              {formatCurrency(borrowTotal)}
            </span>
          </div>
        </div>
      </div>
      <MyDealList lendItems={lendItems} borrowItems={borrowItems} />
      <Navbar />
    </div>
  );
}
