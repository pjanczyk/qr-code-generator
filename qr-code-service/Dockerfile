FROM python:3.8.3

WORKDIR /app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

RUN python -m grpc_tools.protoc --proto_path=protos --python_out=. --grpc_python_out=. protos/qr_code_service.proto

EXPOSE 5000

CMD [ "python", "-u", "server.py" ]
