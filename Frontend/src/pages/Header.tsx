import { useState } from 'react';

export function Header() {
  const [count, setCount] = useState(0);
  return (
    <button
      type="button"
      onClick={() => {
        setCount((prev) => prev + 1);
      }}
    >
      {count}
    </button>
  );
}
