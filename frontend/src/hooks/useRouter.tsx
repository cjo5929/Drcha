import { createBrowserRouter } from 'react-router-dom';

import { Layout } from '@/components/Layout/Layout';
import { Account } from '@/pages/Auth/Account';
import { AccountSend } from '@/pages/Auth/AccountSend';
import { Auth } from '@/pages/Auth/Auth';
import { Complete } from '@/pages/Auth/Complete';
import { Number } from '@/pages/Auth/Number';
import { PhoneNumber } from '@/pages/Auth/PhoneNumber';
import { Login } from '@/pages/Login/Login';
import { Main } from '@/pages/Main';
import { Mypage } from '@/pages/Mypage/Mypage';
import { TransactionDetail } from '@/pages/TransactionDetail/TransactionDetail';

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
        {
          path: 'mypage',
          element: <Mypage />,
        },
        {
          path: 'detail',
          element: <TransactionDetail />,
        },
        {
          path: 'auth',
          element: <Auth />,
          children: [
            {
              path: 'account',
              element: <Account />,
            },
            {
              path: 'account-send',
              element: <AccountSend />,
            },
            {
              path: 'number',
              element: <Number />,
            },
            {
              path: 'phone-number',
              element: <PhoneNumber />,
            },
            {
              path: 'complete',
              element: <Complete />,
            },
          ],
        },
      ],
    },
  ]);
