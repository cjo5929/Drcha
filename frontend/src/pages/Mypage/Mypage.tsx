import { useEffect, useState } from 'react';

import { Navbar } from '@/components/Navbar/Navbar';
import styles from '@/pages/Mypage/Mypage.module.scss';
import { API } from '@/services/api';
import { MyInfo } from '@/types/MyInfo';

import { MyDealList } from './MyDealList';
import { Myinfo } from './Myinfo';

export function Mypage() {
  const [myData, setMyData] = useState<MyInfo | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchMemberInfo = async () => {
      try {
        setIsLoading(true);
        const response = await API.get<MyInfo>('/member/info');

        setMyData(response.data);
      } catch (e) {
        setError('Failed to fetch member info');
      } finally {
        setIsLoading(false);
      }
    };

    fetchMemberInfo();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div className={styles.container}>
      {myData && <Myinfo myInfos={myData} />}
      <div className={styles.sumofmoney}>
        <div className={styles.moneydetail}>
          <div>
            빌려준 돈거래
            <span className={styles.bluespan}> 3건</span>
          </div>
          <div>
            <span className={styles.bluespan}>20413원</span>
          </div>
        </div>
        <div className={styles.moneydetail}>
          <div>
            빌린 돈거래
            <span className={styles.redspan}> 3건</span>
          </div>
          <div>
            <span className={styles.redspan}>1023원</span>
          </div>
        </div>
      </div>
      <MyDealList />
      <Navbar />
    </div>
  );
}
