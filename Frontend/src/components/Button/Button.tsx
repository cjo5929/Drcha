import classNames from 'classnames';

import styles from './Button.module.scss';

type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  color: 'blue' | 'lightblue' | 'red' | 'lightred';
  size: 'large' | 'small' | 'long';
};

export function Button({ color, size, ...rest }: ButtonProps) {
  const { type = 'button', disabled = false, children, className } = rest;
  const buttonClass = classNames(className, styles.button, {
    [styles.blue]: color === 'blue',
    [styles.lightblue]: color === 'lightblue',
    [styles.red]: color === 'red',
    [styles.lightred]: color === 'lightred',
    [styles.large]: size === 'large',
    [styles.small]: size === 'small',
    [styles.long]: size === 'long',
    [styles.disabled]: disabled,
  });

  return (
    <button type={type} disabled={disabled} {...rest} className={buttonClass}>
      {children}
    </button>
  );
}
