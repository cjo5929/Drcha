# 빌드 단계
FROM node:22 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .

# .env 파일을 빌드 시 사용
COPY .env .env

RUN npm run build

# 실행 단계
FROM node:22-alpine
WORKDIR /app
COPY --from=build /app/dist ./dist
COPY --from=build /app/.env ./.env

# serve 설치 및 실행
RUN npm install -g serve
EXPOSE 3000
# 환경 변수를 사용하여 serve 실행
CMD ["sh", "-c", "env $(cat .env | xargs) serve -s dist -l 3000"]