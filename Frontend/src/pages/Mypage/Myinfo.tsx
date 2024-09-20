import { useEffect, useState } from 'react';

export function Myinfo() {
  //유저 정보 받아오고 isCertificated 설정하기
  useEffect(() => {}, []);

  const [isCertificated, setIsCertificated] = useState(false);

  return (
    <div>
      <div className="kakaoinfo">
        <div className="kakaoprofileimg">이미지</div>
        <div className="kakaoprofiledetail">
          <div className="kakaonickname">OOO님</div>
          <div className="kakaoemail">asdf1234@naver.com</div>
        </div>
      </div>
      <div className="accountinfo">
        {/* 인증하기 전, 인증한 후 따로 만들어야 함 */}
        {isCertificated ? '인증 전' : '인증 후'}
      </div>
      <div className="accountbtn">
        <button>계좌이용내역</button>
        <button>대표계좌변경</button>
      </div>
    </div>
  );
}
