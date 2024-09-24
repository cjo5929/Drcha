import { useState } from 'react';

import styles from '@/pages/Mypage/Mypage.module.scss';
import { TransactionHistory } from '@/types/history';

import { ListDetail } from './ListDetail';

export function MyDealList() {
  const [activeTab, setActiveTab] = useState<'lend' | 'borrow'>('lend');
  const [lendExpanding, setLendExpanding] = useState<{
    [key: string]: boolean;
  }>({});
  const [borrowExpanding, setBorrowExpanding] = useState<{
    [key: string]: boolean;
  }>({});

  const lendItems: Array<TransactionHistory> = [
    { name: '강민서', cost: 20000, dates: '2024.12.12', types: '상환 예정' },
    { name: '강민서', cost: 20000, dates: '2024.12.12', types: '연체 중' },
    { name: '강민서', cost: 20000, dates: '2024.12.12', types: '완료' },
  ];

  const borrowItems: Array<TransactionHistory> = [
    { name: '김민수', cost: 15000, dates: '2024.11.15', types: '상환 예정' },
    { name: '김민수', cost: 15000, dates: '2024.11.15', types: '연체 중' },
    { name: '김민수', cost: 15000, dates: '2024.11.15', types: '완료' },
  ];

  const handleTabClick = (tab: 'lend' | 'borrow') => {
    setActiveTab(tab);
  };

  const toggleLendExpanding = (types: string) => {
    setLendExpanding((prev) => ({ ...prev, [types]: !prev[types] }));
  };

  const toggleBorrowExpanding = (types: string) => {
    setBorrowExpanding((prev) => ({ ...prev, [types]: !prev[types] }));
  };

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
              items={lendItems}
              types="상환 예정"
              expanding={lendExpanding['상환 예정'] || false}
              onToggle={() => toggleLendExpanding('상환 예정')}
            />
            <ListDetail
              items={lendItems}
              types="연체 중"
              expanding={lendExpanding['연체 중'] || false}
              onToggle={() => toggleLendExpanding('연체 중')}
            />
            <ListDetail
              items={lendItems}
              types="완료"
              expanding={lendExpanding['완료'] || false}
              onToggle={() => toggleLendExpanding('완료')}
            />
          </>
        ) : (
          <>
            <ListDetail
              items={borrowItems}
              types="상환 예정"
              expanding={borrowExpanding['상환 예정'] || false}
              onToggle={() => toggleBorrowExpanding('상환 예정')}
            />
            <ListDetail
              items={borrowItems}
              types="연체 중"
              expanding={borrowExpanding['연체 중'] || false}
              onToggle={() => toggleBorrowExpanding('연체 중')}
            />
            <ListDetail
              items={borrowItems}
              types="완료"
              expanding={borrowExpanding['완료'] || false}
              onToggle={() => toggleBorrowExpanding('완료')}
            />
          </>
        )}
      </div>
    </div>
  );
}
