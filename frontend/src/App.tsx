import { RouterProvider } from 'react-router-dom';

import { UserProvider } from '@/contexts/user';
import { useRouter } from '@/hooks/useRouter';

import './reset.css';

function App() {
  const router = useRouter();

  return (
    <UserProvider>
      <RouterProvider router={router} />
    </UserProvider>
  );
}

export default App;
