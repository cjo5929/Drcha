import { useState } from 'react';

import styles from './TransactionDetail.module.scss';

export function TransactionAlarm() {
  const [isToggled, setIsToggled] = useState(false);
  const [selectedOption, setSelectedOption] = useState('');
  const [nothing, setNothing] = useState(false);

  const handleToggle = () => {
    if (isToggled) {
      setSelectedOption('');
    }
    setIsToggled(!isToggled);
  };

  const handleNothing = () => {
    setNothing(!nothing);
  };

  const handleOptionChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSelectedOption(e.target.value);
  };

  return (
    <div className={styles.alarms}>
      <div className={styles.alarmsetting}>
        <div className={styles.alarmtitle}>연체 알림 전송 설정</div>
        <div
          className={`${styles.toggleContainer} ${isToggled ? styles.active : ''}`}
          onClick={handleToggle}
          role="button"
          tabIndex={0}
          aria-label="Toggle alarm settings"
          onKeyDown={(e) => {
            if (e.key === 'Enter' || e.key === ' ') {
              handleNothing();
            }
          }}
        >
          <div className={styles.toggleCircle} />
        </div>
      </div>
      <div
        className={`${styles.radioContainer} ${isToggled ? styles.show : styles.hide}`}
      >
        <form>
          <div className={styles.labels}>
            <input
              type="radio"
              value="주 1회(월요일 오전 9시 발송)"
              checked={selectedOption === '주 1회(월요일 오전 9시 발송)'}
              onChange={handleOptionChange}
            />
            주 1회(월요일 오전 9시 발송)
          </div>
          <div className={styles.labels}>
            <input
              type="radio"
              value="주 3회(월, 수, 금 오전 9시 발송)"
              checked={selectedOption === '주 3회(월, 수, 금 오전 9시 발송)'}
              onChange={handleOptionChange}
            />
            주 3회(월, 수, 금 오전 9시 발송)
          </div>
          <div className={styles.labels}>
            <input
              type="radio"
              value="주 5회(평일 오전 9시 발송)"
              checked={selectedOption === '주 5회(평일 오전 9시 발송)'}
              onChange={handleOptionChange}
            />
            주 5회(평일 오전 9시 발송)
          </div>
          <div className={styles.labels}>
            <input
              type="radio"
              value="매일(오전 9시 발송)"
              checked={selectedOption === '매일(오전 9시 발송)'}
              onChange={handleOptionChange}
            />
            매일(오전 9시 발송)
          </div>
        </form>
      </div>
    </div>
  );
}
