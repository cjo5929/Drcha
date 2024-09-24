import { useState } from 'react';

import DownSVG from '@/assets/icons/downarrow.svg?react';
import RightSVG from '@/assets/icons/rightarrow.svg?react';
import styles from '@/pages/Mypage/Mypage.module.scss';

export function ListDetail({
  items,
  types,
}: {
  items: Array<object>;
  types: string;
}) {
  const [expanding, setExpanding] = useState<boolean>(false);

  const handleCategoryToggle = () => {
    setExpanding(!expanding);
  };

  return (
    <div className={styles.category}>
      <button onClick={() => handleCategoryToggle()}>
        <span>
          {expanding ? <DownSVG /> : <RightSVG />}
          &nbsp;
          {types}
        </span>
      </button>
      {expanding && (
        <div className={styles.details}>
          {/* 내역 컴포넌트 - 돌려쓰기 가능 */}
          {/* <DealItem type={activeTab} status="pending" /> */}
        </div>
      )}
    </div>
  );
}
