FROM ubuntu:latest
LABEL authors="safiy"

ENTRYPOINT ["top", "-b"]