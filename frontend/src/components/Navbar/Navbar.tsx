import { NavLink } from 'react-router-dom';

import ChaticonSVG from '@/assets/icons/chaticon.svg?react';
import MypageiconSVG from '@/assets/icons/mypageicon.svg?react';
import StarticonSVG from '@/assets/icons/starticon.svg?react';
import { API } from '@/services/api';
import { Invitation } from '@/types/Invitation';

import styles from './Navbar.module.scss';

const { Kakao } = window;

Kakao.init('9ae699b3eb808fa55f9f4594c66f3be4');
const kakaoShare = async () => {
  try {
    const response = await API.post<Invitation>('/chat');
    // const chatroomID = response.data.chatRoomId;
    const invitationlink = response.data.invitationLink;

    console.log('Chatroom ID:', invitationlink);

    Kakao.Share.sendCustom({
      templateId: 112872,
      templateArgs: {
        DYNAMIC_LINK: `http://localhost:5173/api/v1/chat/${invitationlink}/link/enter`,
      },
    });
  } catch (error) {
    console.error('Error fetching chatroom ID:', error);
  }
};

export function Navbar() {
  return (
    <div className={styles.navbar}>
      <NavLink className={styles.navItem} to="/">
        <ChaticonSVG />
        채팅
      </NavLink>
      <div className={styles.navItem} onClick={kakaoShare}>
        <StarticonSVG />
        거래 시작
      </div>
      <NavLink className={styles.navItem} to="/mypage">
        <MypageiconSVG />
        마이페이지
      </NavLink>
    </div>
  );
}
