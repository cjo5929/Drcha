import { useState } from 'react';

import LeftArrowSVG from '@/assets/icons/leftArrow.svg?react';
import RightArrowSVG from '@/assets/icons/rightarrow.svg?react';
import WarningSVG from '@/assets/icons/warning.svg?react';
import DOCTOR_IMAGE from '@/assets/images/dr,chayongV1.png';
import { MenuProps } from '@/types/MenuProps';

import styles from './Menu.module.scss';

export function Menu({ isOpen, onClose, onOpenModal }: MenuProps) {
  const [currentView, setCurrentView] = useState<'main' | 'transactions'>(
    'main',
  );

  const handleCreate = () => {
    onClose();
    onOpenModal('create');
  };

  const handleCorrection = () => {
    onClose();
    onOpenModal('correction');
  };

  const handleCheck = () => {
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
        <div className={styles.menudetail}>
          <div onClick={handleCreate} className={styles.create}>
            <div className={styles.createtitle}>
              차용증 스마트 작성
              <RightArrowSVG />
            </div>
            <img
              src={DOCTOR_IMAGE}
              alt="doctor"
              className={styles.doctorprofile}
            />
            <div className={styles.createdetail}>
              <WarningSVG width="12" height="12" />
              &nbsp;대화 내용을 기반으로 박사님이 차용증을 대신 작성해줍니다.
            </div>
          </div>
          <div onClick={handleCorrection} className={styles.normalmenu}>
            차용증 수정
            <RightArrowSVG />
          </div>
          <div onClick={handleCheck} className={styles.normalmenu}>
            차용증 확인 및 동의
            <RightArrowSVG />
          </div>
          <div onClick={handleTransactions} className={styles.normalmenu}>
            거래 내역
            <RightArrowSVG />
          </div>
        </div>
      ) : (
        <div className={styles.transactions}>
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
