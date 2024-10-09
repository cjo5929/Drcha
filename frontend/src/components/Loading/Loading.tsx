import LoadingSVG from '@/assets/icons/loading.svg?react';

import styles from './Loading.module.scss';

export function Loading({ size = 24 }: { size?: number }) {
  return (
    <div className={styles.container}>
      <LoadingSVG
        className={styles.loading}
        width={size}
        height={size}
        fill="#d9d9d9"
      />
    </div>
  );
}
