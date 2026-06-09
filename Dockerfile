# 1단계: 빌드
#FROM : 베이스 이미지 (Gradle 버전 + JDK 버전)
FROM gradle:8.14.3-jdk17

#WORKDIR : 작업 디렉토리를 지정한다. 해당 디렉토리가 없으면 새로 생성한다
WORKDIR /app

COPY . .
#[gradlew로 빌드] -> jar 만들기
RUN chmod +x gradlew
RUN ./gradlew build -x test

# 2단계: 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 단계 없음
# 로컬에서 미리 빌드된 jar를 그대로 복사
COPY jobtrace-0.0.1-SNAPSHOT.jar .

#실행
CMD ["java", "-jar", "jobtrace-0.0.1-SNAPSHOT.jar"]