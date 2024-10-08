import { useState } from 'react';

import LeftArrowSVG from '@/assets/icons/leftArrow.svg?react';
import RightArrowSVG from '@/assets/icons/rightarrow.svg?react';
import WarningSVG from '@/assets/icons/warning.svg?react';
import DOCTOR_IMAGE from '@/assets/images/dr,chayongV1.png';
import { IouDatainChatroom } from '@/types/iou';
import { MenuProps } from '@/types/MenuProps';

import styles from './Menu.module.scss';

export function Menu({ isOpen, onClose, onOpenModal, curIou }: MenuProps) {
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

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR');
  };

  const formatCurrency = (amount: number) =>
    amount.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' });

  const getStatusText = (status: IouDatainChatroom['contractStatus']) => {
    const statusMap: Record<IouDatainChatroom['contractStatus'], string> = {
      DRAFTING: '작성 중',
      ACTIVE: '진행 중',
      COMPLETED: '완료',
      OVERDUE: '연체',
      CANCELLED: '취소됨',
    };
    return statusMap[status];
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
            차용증 수동 작성
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
          {curIou && curIou.length > 0 ? (
            <ul className={styles.transactionList}>
              {curIou.map((iou) => (
                <li key={iou.iouId} className={styles.transactionItem}>
                  <h3>차용증 ID: {iou.iouId}</h3>
                  <p>금액: {formatCurrency(iou.iouAmount)}</p>
                  <p>시작일: {formatDate(iou.contractStartDate)}</p>
                  <p>만기일: {formatDate(iou.contractEndDate)}</p>
                  <p>이자율: {iou.interestRate}%</p>
                  <p>총 상환액: {formatCurrency(iou.totalAmount)}</p>
                  <p>상태: {getStatusText(iou.contractStatus)}</p>
                  <p>
                    차용인 동의:{' '}
                    {iou.borrowerAgreement ? '동의함' : '동의 안함'}
                  </p>
                  <p>
                    대여인 동의: {iou.lenderAgreement ? '동의함' : '동의 안함'}
                  </p>
                </li>
              ))}
            </ul>
          ) : (
            <p className={styles.noTransactions}>거래 내역이 없습니다.</p>
          )}
        </div>
      )}
    </div>
  );
}
