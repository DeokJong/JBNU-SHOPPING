#=================================================================
# BUILD STAGE 1 - Next.js 빌드
#=================================================================
FROM node:18-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 패키지 관리 파일 복사
COPY package.json yarn.lock ./

# 종속성 설치
RUN yarn install --frozen-lockfile

# 소스 코드 복사
COPY . .

# Next.js 애플리케이션 빌드
RUN yarn build

#=================================================================
# BUILD STAGE 2 - 경량 런타임 컨테이너 생성
#=================================================================
FROM node:18-alpine AS runner

# 런타임 환경 변수 설정
ENV NODE_ENV=production

# 작업 디렉토리 설정
WORKDIR /app

# Next.js 빌드 파일 및 패키지 설치
COPY --from=builder /app/package.json ./
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public
COPY --from=builder /app/node_modules ./node_modules

# 포트 노출
ENV PORT=80
EXPOSE 80

# 컨테이너 시작 명령어
CMD ["yarn", "start"]
