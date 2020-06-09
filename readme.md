# QR Code Generator

### Demo

[qrcode.piotrjanczyk.com](http://qrcode.piotrjanczyk.com/)

<img src="docs/screenshot.png" width="600"/>

### Architecture

<img src="docs/architecture-diagram.png" width="600"/>

### Implementation details

_**qr-code-service**_ is a microservice implemented in Python which uses [qrcode](https://github.com/lincolnloop/python-qrcode) library.
It provides a gRPC interface for generating QR codes.

_**web**_ is a web application written in Spring and Kotlin. It uses Thymeleaf for server-side rendering.

Both services are dockerized.
Docker Compose configuration is used for deployment on AWS Elastic Container Service.

### UI Design

[Figma project](https://www.figma.com/file/m0zkjHTBtYOHYB327GsUou/QR_Code_Generator?node-id=0%3A1)

### Local development

All services can be run with Docker Compose:

```bash
docker-compose up --build
```

### Deployment

See [`deploy.sh`](deploy.sh) script.

It builds and deploys to [qrcode.piotrjanczyk.com](http://qrcode.piotrjanczyk.com/):
* Builds Docker images
* Pushes them to Docker Hub
* Deploys them to AWS ECS
