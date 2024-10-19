#!/usr/bin/zsh
docker run -d \
    --name db \
    -e POSTGRES_USER=apisecurity \
    -e POSTGRES_PASSWORD=apisecurity \
    -e POSTGRES_DB=apisecurity_demo \
    -p 5460:5432 \
    postgres:13