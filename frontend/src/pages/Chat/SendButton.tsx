interface SendButtonProps {
  onClick: () => void;
}

export function SendButton({ onClick }: SendButtonProps) {
  return (
    <button
      onClick={onClick}
      style={{
        background: 'none',
        border: 'none',
        cursor: 'pointer',
      }}
    >
      <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M20.33 3.67L3.67 10.45C3.3 10.61 3.1 11.05 3.25 11.42L5.24 16.43C5.33 16.67 5.56 16.83 5.81 16.83C5.89 16.83 5.97 16.82 6.05 16.79L9.83 15.22L14.18 19.57C14.34 19.73 14.55 19.82 14.78 19.82C14.87 19.82 14.96 19.81 15.05 19.78C15.35 19.69 15.57 19.43 15.62 19.12L20.91 4.33C21.01 3.96 20.7 3.57 20.33 3.67ZM15.37 17.13L11.25 13.01L17.87 6.39L7.19 13.55L6.07 11.16L19.69 5.63L15.37 17.13Z"
          fill="#007BFF"
        />
      </svg>
    </button>
  );
}
