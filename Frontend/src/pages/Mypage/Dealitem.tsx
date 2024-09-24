function Dealitem({ type, status }: { type: string; status: string }) {
  return (
    <div className={styles.dealItem}>
      {/* 내역 정보를 여기에 출력 */}
      <div>
        {type === 'lend' ? '빌려준' : '빌린'} 돈 - 상태:
        {status}
      </div>
    </div>
  );
}
