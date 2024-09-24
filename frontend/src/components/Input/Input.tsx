import SearchSVG from '@/assets/icons/search.svg?react';

import styles from './Input.module.scss';

type InputProps = React.InputHTMLAttributes<HTMLInputElement> & {
  type: 'chatting' | 'search';
};

export function Input({ type }: InputProps) {
  return (
    <div className={styles.container}>
      {type === 'search' && (
        <SearchSVG width={24} height={24} className={styles.icon} />
      )}
      <input
        className={styles[type]}
        placeholder={type === 'search' ? '사람 이름 검색' : '채팅을 입력하세요'}
      />
    </div>
  );
}
