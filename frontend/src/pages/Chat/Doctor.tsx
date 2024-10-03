import DOCTOR_IMAGE from '@/assets/images/dr,chayongV1.png';

import styles from './Chat.module.scss';

export function Doctor({ content }: { content: string }) {
  return (
    <div className={styles.doctor}>
      <div className={styles.doctorinfo}>
        <img src={DOCTOR_IMAGE} alt="doctor" className={styles.doctorprofile} />
        <div className={styles.doctorname}>차용박사</div>
      </div>
      <div className={styles.doctorbubbles}>
        <div className={styles.doctorbubble}>
          <div className={styles.doctorbubblearrow} />
          {content}
        </div>
      </div>
    </div>
  );
}
