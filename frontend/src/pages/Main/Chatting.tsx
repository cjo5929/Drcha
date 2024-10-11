import { useEffect, useState, useRef } from 'react';
import { NavLink } from 'react-router-dom';

import DangerIcon from '@/assets/icons/dangeruser.svg?react';
import FirstIcon from '@/assets/icons/firstuser.svg?react';
import SafeIcon from '@/assets/icons/safeuser.svg?react';
import WarningIcon from '@/assets/icons/warning.svg?react';
import AVATAR_IMAGE from '@/assets/images/avatar.png';
import { STATUS } from '@/constants/chatting';
import { ChatRoom } from '@/types/Chat';

import styles from './Main.module.scss';

type ChattingProps = {
  chat: ChatRoom;
};

export function Chatting({ chat }: ChattingProps) {
  const {
    chatRoomId,
    name,
    avatarUrl,
    contractStatus,
    lastMessage,
    iouAmount,
    daysUntilDue,
    unreadCount,
    memberTrustInfoResponse,
  } = chat;
  const [status, setStatus] = useState('');
  const [creditStatus, setCreditStatus] = useState('');
  const [showTooltip, setShowTooltip] = useState(false);
  const tooltipRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (contractStatus === STATUS.ACTIVE) {
      setStatus(`D-${daysUntilDue}일`);
    } else if (contractStatus === STATUS.OVERDUE) {
      setStatus(`${-1 * daysUntilDue}일 연체중`);
    } else if (contractStatus === STATUS.DRAFTING) {
      setStatus('거래 중');
    } else {
      setStatus('상환 완료');
    }

    if (memberTrustInfoResponse.lateTrades > 0) {
      setCreditStatus('연체중');
    } else if (memberTrustInfoResponse.debtTrades > 1) {
      setCreditStatus('다중채무자');
    } else if (memberTrustInfoResponse.completedTrades === 0) {
      setCreditStatus('첫거래');
    } else {
      setCreditStatus('안전');
    }
  }, [contractStatus, daysUntilDue, memberTrustInfoResponse]);

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (
        tooltipRef.current &&
        !tooltipRef.current.contains(event.target as Node)
      ) {
        setShowTooltip(false);
      }
    }

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const getCreditStatusStyle = () => {
    switch (creditStatus) {
      case '연체중':
        return styles.creditStatusRed;
      case '다중채무자':
        return styles.creditStatusYellow;
      case '첫거래':
        return styles.creditStatusBlue;
      case '안전':
        return styles.creditStatusGreen;
      default:
        return '';
    }
  };

  const getTooltipMessage = () => {
    switch (creditStatus) {
      case '연체중':
        return '연체자입니다. 주의하세요.';
      case '다중채무자':
        return '다중 채무자입니다. 주의가 필요합니다.';
      case '첫거래':
        return '첫 거래 사용자입니다.';
      case '안전':
        return '안전한 거래 내역을 가진 사용자입니다.';
      default:
        return '';
    }
  };

  const getCreditStatusIcon = () => {
    switch (creditStatus) {
      case '연체중':
        return <DangerIcon className={styles.warningIcon} />;
      case '다중채무자':
        return <WarningIcon className={styles.warningIcon} />;
      case '안전':
        return <SafeIcon className={styles.warningIcon} />;
      case '첫거래':
        return <FirstIcon className={styles.warningIcon} />;
      default:
        return null;
    }
  };

  return (
    <NavLink to={`chat/${chatRoomId}`} className={styles.chatting}>
      {unreadCount > 0 && (
        <div className={styles.unreadCount}>{unreadCount}</div>
      )}
      <div className={styles.chattingBody}>
        <img
          src={avatarUrl || AVATAR_IMAGE}
          alt={name}
          className={styles.avatarImage}
        />
        <div className={styles.chattingContent}>
          <div className={styles.username}>
            <div className={styles.name}>{name}</div>
            <div
              className={`${styles.credits} ${getCreditStatusStyle()}`}
              onClick={(e) => {
                e.preventDefault();
                setShowTooltip(!showTooltip);
              }}
            >
              {getCreditStatusIcon()}
              {creditStatus}
            </div>
            {showTooltip && (
              <div className={styles.tooltip} ref={tooltipRef}>
                {getTooltipMessage()}
              </div>
            )}
          </div>
          <div className={styles.chat}>{lastMessage || ''}</div>
        </div>
      </div>
      <div className={styles.chatStatus}>
        <div className={styles.statusDetail}>
          {contractStatus === STATUS.ACTIVE && (
            <span className={styles.statusDday}>상환일까지 </span>
          )}
          {status}
        </div>
        <div>{iouAmount ? `${iouAmount.toLocaleString('ko-KR')}원` : ''}</div>
      </div>
    </NavLink>
  );
}
