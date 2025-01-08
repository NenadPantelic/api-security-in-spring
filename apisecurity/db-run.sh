#!/usr/bin/zsh
docker run -d \
    --name apisecurity \
    -e POSTGRES_USER=root \
    -e POSTGRES_PASSWORD=root \
    -e POSTGRES_DB=apisecurity_demo \
    -p 5460:5432 \
    -v ./sql:/docker-entrypoint-initdb.d \
    postgres:13