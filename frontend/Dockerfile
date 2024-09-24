# 빌드 단계
FROM node:22 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# 실행 단계
FROM node:22-alpine
WORKDIR /app
COPY --from=build /app/dist ./dist

# serve 설치 및 실행
RUN npm install -g serve
EXPOSE 3000
CMD ["serve", "-s", "dist", "-l", "3000"]