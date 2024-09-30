import { useState } from 'react';

import LeftArrowSVG from '@/assets/icons/leftArrow.svg?react';
import { MenuProps } from '@/types/MenuProps';

import styles from './Menu.module.scss';

export function Menu({ isOpen, onClose, onOpenModal }: MenuProps) {
  const [currentView, setCurrentView] = useState<'main' | 'transactions'>(
    'main',
  );

  const handleCreateIou = () => {
    onClose();
    onOpenModal('create');
  };

  const handleCheckIou = () => {
    onClose();
    onOpenModal('check');
  };

  const handleTransactions = () => {
    setCurrentView('transactions');
  };

  const handleBack = () => {
    setCurrentView('main');
  };

  return (
    <div className={`${styles.menu} ${isOpen ? styles.open : ''}`}>
      {currentView === 'main' ? (
        <ul>
          <li onClick={handleCreateIou}>차용증 작성</li>
          <li onClick={handleCheckIou}>차용증 확인</li>
          <li onClick={handleTransactions}>거래 내역</li>
        </ul>
      ) : (
        <div>
          <LeftArrowSVG
            fill="black"
            onClick={handleBack}
            className={styles.backButton}
          />
          <ul>
            <li>거래 내역 1</li>
            <li>거래 내역 2</li>
            <li>거래 내역 3</li>
          </ul>
        </div>
      )}
    </div>
  );
}
