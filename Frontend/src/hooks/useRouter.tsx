import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components/Layout/Layout';
import { Login } from '@/pages/Login/Login';
import { Main } from '@/pages/Main';
import { Mypage } from '@/pages/Mypage/Mypage';

export const useRouter = () =>
  createBrowserRouter([
    {
      path: '/',
      element: <Layout />,
      children: [
        { index: true, element: <Main /> },
        {
          path: 'login',
          element: <Login />,
        },
        { path: 'mypage', element: <Mypage /> },
      ],
    },
  ]);
