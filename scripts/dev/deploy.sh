#!/bin/bash
BASE_PATH="/home/ubuntu/barlow-server"

BUILD_JAR_FILE=$(ls $BASE_PATH/*.jar)
JAR_NAME=$(basename "$BUILD_JAR_FILE")
echo "> build 파일명: $JAR_NAME"

LOG_PATH="$BASE_PATH/log"
DEPLOY_LOG="$LOG_PATH/deploy.log"
APP_LOG="$LOG_PATH/nohup.out"

CURRENT_TIME=$(date +%c)

echo "$CURRENT_TIME >  build 파일 복사" >> $DEPLOY_LOG
DEPLOY_PATH=$BASE_PATH/deploy-jar/
cp "$BUILD_JAR_FILE" $DEPLOY_PATH

echo "> barlow-api-server-deploy.jar 교체"
CP_JAR_PATH=$DEPLOY_PATH$JAR_NAME
APPLICATION_JAR_NAME=barlow-api-server-deploy.jar
APPLICATION_JAR=$DEPLOY_PATH$APPLICATION_JAR_NAME

echo "> 심볼릭 링크 설정"
ln -Tfs "$CP_JAR_PATH" $APPLICATION_JAR

SPRING_PROFILES_ACTIVE="dev"
LOG4J_CONTEXT_SELECTOR="org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"

SENTRY_ENVIRONMENT=$SPRING_PROFILES_ACTIVE nohup java -jar \
  -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
  -DLog4jContextSelector=$LOG4J_CONTEXT_SELECTOR \
  -Dlog4j2.enable.threadlocals=true \
  -Dlog4j2.enable.direct.encoders=true \
  "$APPLICATION_JAR" > $APP_LOG 2>&1 &

echo "$CURRENT_TIME > build jar 파일 실헹" >> $DEPLOY_LOG
