import { useState } from 'react';

import styles from '@/pages/Mypage/Mypage.module.scss';

import { ListDetail } from './ListDetail';

export function MyDealList() {
  const [activeTab, setActiveTab] = useState<'lend' | 'borrow'>('lend');
  const [lendItems] = useState<Array<object>>([
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '상환 예정',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '상환 예정',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '연체 중',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '연체 중',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '연체 중',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '연체 중',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '완료',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '완료',
    },
  ]);

  const [borrowItems] = useState<Array<object>>([
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '상환 예정',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '상환 예정',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '연체 중',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '연체 중',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '연체 중',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '연체 중',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '완료',
    },
    {
      name: '강민서',
      cost: 20000,
      dates: 2024 - 34 - 12,
      types: '완료',
    },
  ]);

  const handleTabClick = (tab: 'lend' | 'borrow') => {
    setActiveTab(tab);
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
            <ListDetail items={lendItems} types="상환 예정" />
            <ListDetail items={lendItems} types="연체 중" />
            <ListDetail items={lendItems} types="완료" />
          </>
        ) : (
          <>
            <ListDetail items={borrowItems} types="상환 예정" />
            <ListDetail items={borrowItems} types="연체 중" />
            <ListDetail items={borrowItems} types="완료" />
          </>
        )}
      </div>
    </div>
  );
}
