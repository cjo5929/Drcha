# 프론트엔드 빌드 단계
FROM node:22 AS frontend-build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .

# .env 파일을 빌드 시 사용
COPY .env .env

RUN npm run build

# 백엔드 빌드 단계
FROM node:22 AS backend-build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .

# 실행 단계
FROM node:22-alpine
WORKDIR /app

# 프론트엔드 파일 복사
COPY --from=frontend-build /app/dist ./dist
COPY --from=frontend-build /app/.env ./.env

# 백엔드 파일 복사
COPY --from=backend-build /app/package*.json ./
COPY --from=backend-build /app/server.js ./
COPY --from=backend-build /app/fonts ./fonts

# 프로덕션 종속성 설치
RUN npm ci --only=production

# serve 설치
RUN npm install -g serve

# 포트 설정
EXPOSE 3000 3002

# 시작 스크립트 생성
RUN echo '#!/bin/sh' > start.sh && \
    echo 'env $(cat .env | xargs) serve -s dist -l 3000 &' >> start.sh && \
    echo 'node server.js' >> start.sh && \
    chmod +x start.sh

# 두 서비스 실행
CMD ["./start.sh"]
