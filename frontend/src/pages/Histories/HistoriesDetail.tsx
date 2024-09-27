import { useState } from 'react';

import styles from './Histories.module.scss';

export function HistoriesDetail() {
  const [events] = useState([
    { date: '123213', description: '거래 시작' },
    { date: '1111233', description: '얼마 갚음zzzzzzzzzzzzzzzzzzzz' },
    { date: '1111233', description: '얼마 갚음zzzzzzzzzzzzzzzzzzzz' },
    { date: '1111233', description: '얼마 갚음zzzzzzzzzzzzzzzzzzzz' },
    { date: '1111233', description: '얼마 갚음zzzzzzzzzzzzzzzzzzzz' },
    { date: '1111233', description: '얼마 갚음zzzzzzzzzzzzzzzzzzzz' },
    { date: '1111233', description: '얼마 갚음zzzzzzzzzzzzzzzzzzzz' },
    { date: '1111233', description: '얼마 갚음zzzzzzzzzzzzzzzzzzzz' },
    { date: '1111233', description: '얼마 갚음zzzzzzzzzzzzzzzzzzzz' },
    { date: '11111', description: '거래 완료' },
  ]);
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
            <p>{item.date}</p>
            <p>{item.description}</p>
          </div>
        </div>
      ))}
    </div>
  );
}
