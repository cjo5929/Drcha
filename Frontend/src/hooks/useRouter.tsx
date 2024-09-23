import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components/Layout/Layout';
import { Main } from '@/pages/Main';
import { Mypage } from '@/pages/Mypage/Mypage';

export const useRouter = () =>
  createBrowserRouter([
    {
      path: '/',
      element: <Layout />,
      children: [
        { index: true, element: <Main /> },
        { path: 'mypage', element: <Mypage /> },
      ],
    },
  ]);
