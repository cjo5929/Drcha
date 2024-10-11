import styles from './Toast.module.scss';

export function Toast({
  code,
  isVisible,
}: {
  code: string;
  isVisible: boolean;
}) {
  return (
    <div className={`${styles.toast} ${isVisible ? styles.show : styles.hide}`}>
      <div className={styles.code}>차용박사{code}님이 1원을 입금했습니다.</div>
    </div>
  );
}
