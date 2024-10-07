import { useState, useEffect } from 'react';

import { TransactionDetailHistory } from '@/types/history';

import styles from './Histories.module.scss';

export function HistoriesDetail({
  curhistory,
}: {
  curhistory: TransactionDetailHistory[] | undefined;
}) {
  const [events, setEvents] = useState<TransactionDetailHistory[]>([]);
  useEffect(() => {
    if (!curhistory || curhistory.length === 0) {
      throw new Error('no history');
    }
    setEvents(curhistory);
  }, [curhistory]);
  const formatDate = (dateString: string) =>
    new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    });
  return (
    <div className={styles.detailContainer}>
      {events.map((item, index) => (
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
            <p>{formatDate(item.transactionDate)}</p>
            <p>{item.description}</p>
          </div>
        </div>
      ))}
    </div>
  );
}
