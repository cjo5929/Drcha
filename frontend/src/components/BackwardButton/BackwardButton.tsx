import { useNavigate } from 'react-router-dom';

import LeftArrowSVG from '@/assets/icons/leftArrow.svg?react';

export function BackwardButton() {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(-1);
  };
  return <LeftArrowSVG fill="#3288ff" onClick={handleClick} />;
}
