import { RefObject } from 'react';

import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';

export function useDownloadPdf() {
  const SCALE = 3;

  const downloadPdf = async (ref: RefObject<HTMLDivElement>) => {
    if (!ref.current) {
      return;
    }

    const canvas = await html2canvas(ref.current, {
      scale: SCALE,
    });
    const imgData = canvas.toDataURL('image/png');

    const pdf = new jsPDF('p', 'mm', 'a4');
    const imgWidth = pdf.internal.pageSize.getWidth();
    const imgHeight = pdf.internal.pageSize.getHeight();

    pdf.addImage(imgData, 'PNG', 10, 10, imgWidth - 20, imgHeight - 20);
    pdf.save('IOU.pdf');
  };

  const convertToImage = async (ref: RefObject<HTMLDivElement>) => {
    if (!ref.current) {
      return;
    }

    const canvas = await html2canvas(ref.current, {
      scale: SCALE,
    });
    const imgData = canvas.toDataURL('image/png');
    return imgData;
  };

  return { downloadPdf, convertToImage };
}
