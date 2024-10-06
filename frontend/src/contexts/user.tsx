import { createContext, useCallback, useEffect, useState } from 'react';

import { member } from '@/services/member';
import { MyInfo } from '@/types/Member';

interface UserProps {
  isLogin: boolean;
  setIsLogin: React.Dispatch<React.SetStateAction<boolean>>;
  userInfo: MyInfo | undefined;
  setUserInfo: React.Dispatch<React.SetStateAction<MyInfo | undefined>>;
}

type UserProviderProps = {
  children: React.ReactNode;
};

export const UserContext = createContext<UserProps | undefined>(undefined);

export function UserProvider({ children }: UserProviderProps) {
  const [isLogin, setIsLogin] = useState(true);
  const [userInfo, setUserInfo] = useState<MyInfo | undefined>();

  const getMyInfo = useCallback(async () => {
    try {
      const myInfo = await member.getMemberInfo();
      setUserInfo(myInfo);
      setIsLogin(true);
    } catch {
      setUserInfo(undefined);
      setIsLogin(false);
    }
  }, []);

  useEffect(() => {
    getMyInfo();
  }, [getMyInfo]);

  return (
    <UserContext.Provider
      // eslint-disable-next-line react/jsx-no-constructed-context-values
      value={{ isLogin, setIsLogin, userInfo, setUserInfo }}
    >
      {children}
    </UserContext.Provider>
  );
}
