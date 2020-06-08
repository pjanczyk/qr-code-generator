#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

echo "-> Building Docker images..."
docker-compose build

echo "-> Pushing images to Docker registry..."
docker push pjanczyk/qr-code-generator_web
docker push pjanczyk/qr-code-generator_qr-code-service

echo "-> Running containers on ECS..."
ecs-cli compose service up --force-deployment

echo "-> Done"
