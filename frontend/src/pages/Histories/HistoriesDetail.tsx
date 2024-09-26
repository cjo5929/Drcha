import styles from './Histories.module.scss';
import { TimelineEvent } from './TimelineEvent';

export function HistoriesDetail() {
  return (
    <div className={styles.detailContainer}>
      <div className={styles.timeline}>
        {/* 타임라인 시작점 - 거래 시작일 */}
        <div className={styles.startPoint} />
        <TimelineEvent date="2023-01-01" description="거래 시작" icon="📅" />

        <TimelineEvent date="adfad" description="adfafd" icon="a" />

        {/* 타임라인 종료점 - 거래 종료일 */}
        <TimelineEvent date="2023-12-01" description="변제 완료" icon="✅" />
        <div className={styles.endPoint} />
      </div>
    </div>
  );
}
