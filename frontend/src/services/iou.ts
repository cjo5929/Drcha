import { IouData, IouDetailData } from '@/types/iou';

import { API } from './api';

export const iou = {
  endpoint: {
    default: '/iou',
    pdf: '/iou/pdf',
    getLendingRecords: '/iou/lending-records',
    getBorrowingRecords: '/iou/borrowing-records',
    getLendingDetail: '/iou/lending-detail',
    getBorrowingDetail: '/iou/borrowing-detail',
  },

  getIouPdf: async (iouId: string): Promise<IouData> => {
    const { data } = await API.get(`${iou.endpoint.pdf}/${iouId}`);

    return data;
  },

  getLendingRecords: async () => {
    const { data } = await API.get(`${iou.endpoint.getLendingRecords}`);
    return data;
  },

  getBorrowingRecords: async () => {
    const { data } = await API.get(`${iou.endpoint.getBorrowingRecords}`);
    return data;
  },

  getLendingDetail: async (iouId: string): Promise<IouDetailData> => {
    const { data } = await API.get(`${iou.endpoint.getLendingDetail}/${iouId}`);
    return data;
  },

  getBorrowingDetail: async (iouId: string): Promise<IouDetailData> => {
    const { data } = await API.get(
      `${iou.endpoint.getBorrowingDetail}/${iouId}`,
    );
    return data;
  },
};
