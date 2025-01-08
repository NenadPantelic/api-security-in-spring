#!/usr/bin/zsh

docker run --name redis-db -d -p 6379:6379 redis redis-server --save 60 1 --loglevel warning