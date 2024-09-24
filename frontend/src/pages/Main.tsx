import { Button } from '@/components/Button/Button';
import { Input } from '@/components/Input/Input';

import styles from './Main.module.scss';

export function Main() {
  return (
    <div className={styles.container}>
      <Button color="blue" size="long">
        버튼
      </Button>
      <Button color="lightblue" size="long">
        버튼
      </Button>
      <Button color="red" size="long">
        버튼
      </Button>
      <Button color="lightred" size="long">
        버튼
      </Button>
      <Button color="blue" size="large">
        버튼
      </Button>
      <Button color="blue" size="small">
        버튼
      </Button>
      <Input type="chatting" />
      <Input type="search" />
    </div>
  );
}
