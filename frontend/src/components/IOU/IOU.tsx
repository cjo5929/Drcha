import { useRef } from 'react';
import { useNavigate } from 'react-router-dom';

import BackiconSVG from '@/assets/icons/backicon.svg?react';
import DownloadSVG from '@/assets/icons/download.svg?react';
import { useDownloadPdf } from '@/hooks/useDownloadPdf';

import styles from './IOU.module.scss';
import { IOUContent } from './IOUContent';

export function IOU() {
  const navigate = useNavigate();
  const IOURef = useRef<HTMLDivElement>(null);
  const { downloadPdf } = useDownloadPdf();

  const iouData = {
    iouId: 0,
    creditorName: '강민서',
    debtorName: '지종권',
    iouAmount: 300000,
    contractStartDate: '2024-10-05T17:44:50.352Z',
    contractEndDate: '2024-10-05T17:44:50.352Z',
    interestRate: 10,
    borrowerAgreement: true,
    lenderAgreement: true,
    totalAmount: 330000,
  };

  const handleDownloadIOU = async () => {
    if (window.confirm('차용증을 다운로드 하시겠습니까?')) {
      await downloadPdf(IOURef);
    }
  };

  const handleBackButton = () => {
    navigate(-1);
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <button onClick={handleBackButton}>
          <BackiconSVG />
        </button>
        <button onClick={handleDownloadIOU}>
          <DownloadSVG />
        </button>
      </div>
      <IOUContent iouRef={IOURef} iouData={iouData} />
    </div>
  );
}
