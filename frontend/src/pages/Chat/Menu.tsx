import styles from './Menu.module.scss';

export function Menu({ isOpen }: { isOpen: boolean }) {
  return (
    <div className={`${styles.menu} ${isOpen ? styles.open : ''}`}>
      <ul>
        <li>메뉴 항목 1</li>
        <li>메뉴 항목 2</li>
        <li>메뉴 항목 3</li>
      </ul>
    </div>
  );
}
