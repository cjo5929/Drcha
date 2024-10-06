import { IouData } from '@/types/iou';

import { API } from './api';

export const iou = {
  endpoint: {
    default: '/iou',
    pdf: '/iou/pdf',
  },

  getIouPdf: async (iouId: string): Promise<IouData> => {
    const { data } = await API.get(`${iou.endpoint.pdf}/${iouId}`);

    return data;
  },
};
