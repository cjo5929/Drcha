import { useState, useEffect } from 'react';

import styles from '@/pages/Mypage/Mypage.module.scss';
import { TransactionHistory } from '@/types/history';

import { ListDetail } from './ListDetail';

export function MyDealList({
  lendItems,
  borrowItems,
}: {
  lendItems: TransactionHistory[];
  borrowItems: TransactionHistory[];
}) {
  const [activeTab, setActiveTab] = useState<'lend' | 'borrow'>('lend');
  const [lendExpanding, setLendExpanding] = useState<{
    [key: string]: boolean;
  }>({});
  const [borrowExpanding, setBorrowExpanding] = useState<{
    [key: string]: boolean;
  }>({});

  const handleTabClick = (tab: 'lend' | 'borrow') => {
    setActiveTab(tab);
  };

  const toggleLendExpanding = (types: string) => {
    setLendExpanding((prev) => ({ ...prev, [types]: !prev[types] }));
  };

  const toggleBorrowExpanding = (types: string) => {
    setBorrowExpanding((prev) => ({ ...prev, [types]: !prev[types] }));
  };

  useEffect(() => {
    setLendExpanding({
      '상환 예정': true,
      '연체 중': true,
      완료: false,
    });
    setBorrowExpanding({
      '상환 예정': true,
      '연체 중': true,
      완료: false,
    });
  }, []);

  return (
    <div className={styles.listContainer}>
      {/* 상단 버튼 */}
      <div className={styles.tabContainer}>
        <button
          className={`${styles.tabButton} ${activeTab === 'lend' ? styles.active : ''}`}
          onClick={() => handleTabClick('lend')}
        >
          빌려준 기록
        </button>
        <button
          className={`${styles.tabButton} ${activeTab === 'borrow' ? styles.active : ''}`}
          onClick={() => handleTabClick('borrow')}
        >
          빌린 기록
        </button>
      </div>

      {/* 내역 리스트 */}
      <div className={styles.listContent}>
        {activeTab === 'lend' ? (
          <>
            <ListDetail
              items={lendItems.filter(
                (item) => item.contractStatus === 'ACTIVE',
              )}
              types="상환 예정"
              lendorborrow="lend"
              expanding={lendExpanding['상환 예정'] || false}
              onToggle={() => toggleLendExpanding('상환 예정')}
            />
            <ListDetail
              items={lendItems.filter(
                (item) => item.contractStatus === 'OVERDUE',
              )}
              types="연체 중"
              lendorborrow="lend"
              expanding={lendExpanding['연체 중'] || false}
              onToggle={() => toggleLendExpanding('연체 중')}
            />
            <ListDetail
              items={lendItems.filter(
                (item) => item.contractStatus === 'COMPLETED',
              )}
              types="완료"
              lendorborrow="lend"
              expanding={lendExpanding['완료'] || false}
              onToggle={() => toggleLendExpanding('완료')}
            />
          </>
        ) : (
          <>
            <ListDetail
              items={borrowItems.filter(
                (item) => item.contractStatus === 'ACTIVE',
              )}
              types="상환 예정"
              lendorborrow="borrow"
              expanding={borrowExpanding['상환 예정'] || false}
              onToggle={() => toggleBorrowExpanding('상환 예정')}
            />
            <ListDetail
              items={borrowItems.filter(
                (item) => item.contractStatus === 'OVERDUE',
              )}
              types="연체 중"
              lendorborrow="borrow"
              expanding={borrowExpanding['연체 중'] || false}
              onToggle={() => toggleBorrowExpanding('연체 중')}
            />
            <ListDetail
              items={borrowItems.filter(
                (item) => item.contractStatus === 'COMPLETED',
              )}
              types="완료"
              lendorborrow="borrow"
              expanding={borrowExpanding['완료'] || false}
              onToggle={() => toggleBorrowExpanding('완료')}
            />
          </>
        )}
      </div>
    </div>
  );
}
