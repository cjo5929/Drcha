import { useContext } from 'react';

import { UserContext } from '@/contexts/user';

export function useUserState() {
  const context = useContext(UserContext);
  if (context === undefined) {
    throw new Error('useUserState should be used within UserContext');
  }
  const { isLogin, setIsLogin, userInfo, setUserInfo } = context;
  return { isLogin, setIsLogin, userInfo, setUserInfo };
}
