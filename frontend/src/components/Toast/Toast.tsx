import styles from './Toast.module.scss';

export function Toast({
  code,
  accountNo,
  isVisible,
}: {
  code: string;
  accountNo: string;
  isVisible: boolean;
}) {
  return (
    <div className={`${styles.toast} ${isVisible ? styles.show : styles.hide}`}>
      <div className={styles.accountNo}>싸피은행 {accountNo}</div>
      <div className={styles.code}>차용박사{code}님이 1원을 입금했습니다.</div>
    </div>
  );
}
