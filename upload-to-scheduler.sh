#!/bin/bash

REPORT_FILE=$1

if [ ! -f "$REPORT_FILE" ]; then
    echo "Ошибка: Файл $REPORT_FILE не найден."
    exit 1
fi

if [ -z "$SCHEDULER_URL" ]; then
    echo "Ошибка: SCHEDULER_URL не задан"
    exit 1
fi

echo "Отправка файла отчета $REPORT_FILE на сервер..."

HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$SCHEDULER_URL/reports" \
  -F "runId=${GITHUB_RUN_ID}" \
  -F "format=junit" \
  -F "file=@${REPORT_FILE}")

echo "HTTP status: $HTTP_STATUS"

if [ "$HTTP_STATUS" -eq 200 ] || [ "$HTTP_STATUS" -eq 201 ]; then
    echo "Успешно загружено"
else
    echo "ОШИБКА: Сервер вернул $HTTP_STATUS"
fi