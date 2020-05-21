import io
from concurrent.futures import ThreadPoolExecutor

import grpc
import qrcode

import qr_code_service_pb2 as qr_code_pb
import qr_code_service_pb2_grpc as qr_code_grpc


class QrCodeService(qr_code_grpc.QrCodeServiceServicer):
    def CreateQrCode(self, request, context):
        print(request.data)
        image = qrcode.make(request.data)
        byte_io = io.BytesIO()
        image.save(byte_io, format='PNG')
        byte_array = byte_io.getvalue()
        return qr_code_pb.QrCodeResponse(file=byte_array)


def serve():
    print("Starting server...")
    server = grpc.server(ThreadPoolExecutor(max_workers=10))
    qr_code_grpc.add_QrCodeServiceServicer_to_server(QrCodeService(), server)
    server.add_insecure_port('[::]:5000')
    server.start()
    print("Listening on port 5000")
    server.wait_for_termination()


if __name__ == '__main__':
    serve()

print("BB")
