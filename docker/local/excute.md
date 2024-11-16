### 로컬 실행방법
- docker build -f docker/local/Dockerfile . -t concert
- docker pull mysql:8.0.22
- docker pull redis
- docker-compose -f docker/local/docker-compose.yml up
