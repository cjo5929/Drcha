import { useCallback, useEffect, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import BackiconSVG from '@/assets/icons/backicon.svg?react';
import DownloadSVG from '@/assets/icons/download.svg?react';
import { Loading } from '@/components/Loading/Loading';
import { useDownloadPdf } from '@/hooks/useDownloadPdf';
import { iou } from '@/services/iou';
import { IouData } from '@/types/iou';

import styles from './IOU.module.scss';
import { IOUContent } from './IOUContent';

export function IOU() {
  const { iouID } = useParams();
  const navigate = useNavigate();
  const IOURef = useRef<HTMLDivElement>(null);
  const { downloadPdf } = useDownloadPdf();
  const [iouPdfData, setIouPdfData] = useState<IouData | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);

  const getIouData = useCallback(async () => {
    try {
      setIsLoading(true);
      const data = await iou.getIouPdf(iouID!);
      setIouPdfData(data);
    } catch {
      setIsError(true);
    } finally {
      setIsLoading(false);
    }
  }, [iouID]);

  useEffect(() => {
    getIouData();
  }, [getIouData]);

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
      {!isError && !isLoading && iouPdfData && (
        <IOUContent iouRef={IOURef} iouData={iouPdfData} type="page" />
      )}
      {isLoading && <Loading size={100} />}
      {!isLoading && isError && (
        <div className={styles.noContent}>차용증 정보가 없습니다.</div>
      )}
    </div>
  );
}
