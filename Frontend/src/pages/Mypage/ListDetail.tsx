import { useEffect, useState } from 'react';

import DownSVG from '@/assets/icons/downarrow.svg?react';
import RightSVG from '@/assets/icons/rightarrow.svg?react';
import styles from '@/pages/Mypage/Mypage.module.scss';
import { TransactionHistory } from '@/types/history';

import { Dealitem } from './Dealitem';

export function ListDetail({
  items,
  types,
  expanding,
  onToggle,
}: {
  items: Array<TransactionHistory>;
  types: string;
  expanding: boolean;
  onToggle: () => void;
}) {
  const [curItem, setCurItem] = useState<Array<TransactionHistory>>([]);

  useEffect(() => {
    const filteredItems = items.filter((item) => item.types === types);
    setCurItem(filteredItems);
  }, [items, types]);

  return (
    <div className={styles.category}>
      <button onClick={onToggle}>
        <span>
          {expanding ? <DownSVG /> : <RightSVG />}
          &nbsp;
          {types}
        </span>
      </button>
      {expanding && (
        <div className={styles.details}>
          {curItem.map((item) => (
            <Dealitem key={item.name + item.dates} item={item} />
          ))}
        </div>
      )}
    </div>
  );
}
