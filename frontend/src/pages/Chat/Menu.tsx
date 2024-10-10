import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import LeftArrowSVG from '@/assets/icons/leftArrow.svg?react';
import RightArrowSVG from '@/assets/icons/rightarrow.svg?react';
import WarningSVG from '@/assets/icons/warning.svg?react';
import DOCTOR_IMAGE from '@/assets/images/dr,chayongV1.png';
import { IouDatainChatroom } from '@/types/iou';
import { MenuProps } from '@/types/MenuProps';

import styles from './Menu.module.scss';

export function Menu({
  isOpen,
  onClose,
  onOpenModal,
  curIou,
  memberRole,
}: MenuProps) {
  const [currentView, setCurrentView] = useState<'main' | 'transactions'>(
    'main',
  );

  const navigate = useNavigate();

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

  const handleDetail =
    (iou: IouDatainChatroom) => (event: React.MouseEvent) => {
      event.preventDefault();
      if (iou.contractStatus !== 'DRAFTING') {
        navigate(`/detail/${iou.iouId}`);
      }
    };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR');
  };

  const formatCurrency = (amount: number) =>
    `${amount.toLocaleString('ko-KR', {
      maximumFractionDigits: 0,
    })}원`;

  const sortIous = (a: IouDatainChatroom, b: IouDatainChatroom): number => {
    const order = {
      ACTIVE: 0,
      OVERDUE: 1,
      COMPLETED: 2,
      CANCELLED: 3,
      DRAFTING: 4,
    };
    return order[a.contractStatus] - order[b.contractStatus];
  };

  const filteredIous = curIou
    ? curIou.filter((iou) => iou.contractStatus !== 'DRAFTING').sort(sortIous)
    : [];

  const getStatusText = (status: IouDatainChatroom['contractStatus']) => {
    if (status === 'OVERDUE') return '연체 중';
    if (status === 'ACTIVE') return '상환 중';
    if (status === 'COMPLETED') return '상환 완료';
  };

  return (
    <div className={`${styles.menu} ${isOpen ? styles.open : ''}`}>
      {currentView === 'main' ? (
        <div className={styles.menudetail}>
          {memberRole !== 'DEBTOR' && (
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
          )}
          {memberRole !== 'DEBTOR' && (
            <div onClick={handleCorrection} className={styles.normalmenu}>
              차용증 수동 작성
              <RightArrowSVG />
            </div>
          )}
          <div onClick={handleCheck} className={styles.normalmenu}>
            차용증 확인 및 동의
            <RightArrowSVG />
          </div>
          <div onClick={handleTransactions} className={styles.normalmenu}>
            {memberRole === 'DEBTOR' ? '빌린 기록' : '빌려준 기록'}
            <RightArrowSVG />
          </div>
        </div>
      ) : (
        <div className={styles.transactions}>
          <div className={styles.transactionsHeader} onClick={handleBack}>
            <LeftArrowSVG fill="black" className={styles.backButton} />
            {memberRole === 'DEBTOR' ? '빌린 기록' : '빌려준 기록'}
          </div>
          {curIou && filteredIous.length > 0 ? (
            <ul className={styles.transactionList}>
              {filteredIous.map((iou) => (
                <li
                  key={iou.iouId}
                  className={`${styles.transactionItem} ${styles[iou.contractStatus.toLowerCase()]}`}
                  onClick={handleDetail(iou)}
                >
                  <div className={styles.transactionItemOne}>
                    <div className={styles.amount}>
                      {formatCurrency(iou.iouAmount)}
                    </div>
                    <div className={styles.status}>
                      {getStatusText(iou.contractStatus)}
                    </div>
                  </div>
                  <div className={styles.transactionItemTwo}>
                    <p>시작일: {formatDate(iou.contractStartDate)}</p>
                    <p>만기일: {formatDate(iou.contractEndDate)}</p>
                    <p>이자율: {iou.interestRate}%</p>
                  </div>
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
