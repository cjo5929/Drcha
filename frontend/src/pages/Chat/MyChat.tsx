import DOCTOR_IMAGE from '@/assets/images/dr,chayongV1.png';

import styles from './Chat.module.scss';

export function MyChat() {
  return (
    <div className={styles.mychat}>
      <div className={styles.mybubble}>
        <img src={DOCTOR_IMAGE} alt="doctor" />
        <div className={styles.mybubblearrow} />
      </div>
    </div>
  );
}
