declare global {
  interface Window {
    Kakao: {
      init: (string) => void;
      Share: {
        sendCustom: ({
          templateId: number,
          templateArgs: { DYNAMIC_LINK: string },
        }) => void;
      };
    };
  }
}

export {};
