import { NavLink } from 'react-router-dom';

import ChaticonSVG from '@/assets/icons/chaticon.svg?react';
import MypageiconSVG from '@/assets/icons/mypageicon.svg?react';
import StarticonSVG from '@/assets/icons/starticon.svg?react';
import { URL } from '@/constants/url';
import { API } from '@/services/api';
import { Invitation } from '@/types/Invitation';

import styles from './Navbar.module.scss';

const { Kakao } = window;

Kakao.init(`${import.meta.env.VITE_API_KAKAO_ID}`);
const kakaoShare = async () => {
  try {
    const response = await API.post<Invitation>('/chat');
    // const chatroomID = response.data.chatRoomId;
    const invitationlink = response.data.invitationLink;

    Kakao.Share.sendCustom({
      templateId: 112872,
      templateArgs: {
        DYNAMIC_LINK: `${URL.INVITE}/api/v1/chat/${invitationlink}/link/enter`,
      },
    });
  } catch (error) {
    console.error(error);
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
