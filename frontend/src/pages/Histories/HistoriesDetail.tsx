import { useState, useEffect } from 'react';

import { Transaction, TransactionDetailHistory } from '@/types/history';

import styles from './Histories.module.scss';

export function HistoriesDetail({
  curhistory,
}: {
  curhistory: TransactionDetailHistory | undefined;
}) {
  const [events, setEvents] = useState<Transaction[]>([]);
  useEffect(() => {
    if (!curhistory) {
      throw new Error('no history');
    }
    const startTransaction: Transaction = {
      amount: -1,
      balanceAfterTransaction: -1,
      balanceBeforeTransaction: -1,
      description: '차용일',
      transactionDate: curhistory.transactionStartDate,
      transactionId: -1,
      transactionType: 'START',
      transactionUniqueNo: -1,
    };

    const endTransaction: Transaction = {
      amount: -1,
      balanceAfterTransaction: -1,
      balanceBeforeTransaction: -1,
      description: '변제 예정일',
      transactionDate: curhistory.transactionEndDate,
      transactionId: -2,
      transactionType: 'END',
      transactionUniqueNo: -2,
    };

    const allEvents = [
      startTransaction,
      ...curhistory.transactions,
      endTransaction,
    ];

    if (curhistory.repaymentDate) {
      const repaymentTransaction: Transaction = {
        amount: -1,
        balanceAfterTransaction: -1,
        balanceBeforeTransaction: -1,
        description: '변제 완료일',
        transactionDate: curhistory.repaymentDate,
        transactionId: -3,
        transactionType: 'REPAYMENT',
        transactionUniqueNo: -3,
      };
      allEvents.push(repaymentTransaction);
    }

    const sortedEvents = allEvents.sort(
      (a, b) =>
        new Date(a.transactionDate).getTime() -
        new Date(b.transactionDate).getTime(),
    );

    setEvents(sortedEvents);
  }, [curhistory]);
  const addKSTOffset = (date: Date): Date =>
    new Date(date.getTime() + 9 * 60 * 60 * 1000);

  const formatDate = (dateString: string): { date: string; time: string } => {
    const kstDate = addKSTOffset(new Date(dateString));
    const date = kstDate.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      timeZone: 'Asia/Seoul',
    });
    const time = kstDate.toLocaleTimeString('ko-KR', {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      timeZone: 'Asia/Seoul',
    });
    return { date, time };
  };

  const formatCurrency = (amount: number) =>
    `${amount.toLocaleString('ko-KR', {
      maximumFractionDigits: 0,
    })}원`;
  return (
    <div className={styles.detailContainer}>
      {events.map((item, index) => {
        const { date, time } = formatDate(item.transactionDate);
        return (
          <div
            className={`${styles.timelineItem} ${
              index === 0
                ? styles.first
                : index === events.length - 1
                  ? styles.last
                  : ''
            }`}
          >
            <div className={styles.dot} />
            <div className={styles.content}>
              <div className={styles.dateTime}>
                <div className={styles.date}>{date}</div>
                <div className={styles.time}>{time}</div>
              </div>
              <div className={styles.contentMoney}>
                <div className={styles.contentMoneyDescription}>
                  {item.description}
                </div>
                {item.amount !== -1 && (
                  <div className={styles.contentMoneyDetail}>
                    ({formatCurrency(item.amount)} 상환 /{' '}
                    {formatCurrency(item.balanceAfterTransaction)} 남음)
                  </div>
                )}
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}
