import styles from './Chat.module.scss';
import { Doctor } from './Doctor';
import { MyChat } from './MyChat';
import { PartnerChat } from './PartnerChat';

export function ChatContent() {
  return (
    <div className={styles.chatcontainer}>
      <Doctor />
      <PartnerChat />
      <MyChat />
      <PartnerChat />
      <PartnerChat />
    </div>
  );
}
