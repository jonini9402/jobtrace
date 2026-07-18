# 2단계: 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 단계 없음
# 로컬에서 미리 빌드된 jar를 그대로 복사
COPY build/libs/jobtrace-0.0.1-SNAPSHOT.jar .

#실행
CMD ["java", "-jar", "jobtrace-0.0.1-SNAPSHOT.jar"]