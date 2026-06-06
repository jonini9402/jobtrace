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

#1단계에서 .jar만 가져오기
COPY --from=0 /app/build/libs/jobtrace-0.0.1-SNAPSHOT.jar .

#실행
CMD ["java", "-jar", "jobtrace-0.0.1-SNAPSHOT.jar"]