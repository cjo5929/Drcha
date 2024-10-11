import DownSVG from '@/assets/icons/downarrow.svg?react';
import RightSVG from '@/assets/icons/rightarrow.svg?react';
import styles from '@/pages/Mypage/Mypage.module.scss';
import { TransactionHistory } from '@/types/history';

import { Dealitem } from './Dealitem';

export function ListDetail({
  items,
  types,
  lendorborrow,
  expanding,
  onToggle,
}: {
  items: Array<TransactionHistory>;
  types: string;
  lendorborrow: string;
  expanding: boolean;
  onToggle: () => void;
}) {
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
          {items.map((item) => (
            <Dealitem
              key={item.iouId}
              item={item}
              lendorborrow={lendorborrow}
            />
          ))}
        </div>
      )}
    </div>
  );
}
