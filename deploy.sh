#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

echo "-> Building Docker images..."
docker-compose build

echo "-> Pushing images to Docker registry..."
docker push pjanczyk/qr-code-generator_web
docker push pjanczyk/qr-code-generator_qr-code-service

echo "-> Recreating ECS containers..."
ecs-cli compose --file docker-compose.production.yml service stop
sleep 10
ecs-cli compose --file docker-compose.production.yml service start --force-deployment

echo "-> Done"
