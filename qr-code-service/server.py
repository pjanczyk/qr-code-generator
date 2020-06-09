import io
from concurrent.futures import ThreadPoolExecutor

import grpc
import qrcode
import qrcode.image.pil
import qrcode.image.svg

from qr_code_service_pb2 import ErrorCorrection, ImageFormat, QrCodeResponse
from qr_code_service_pb2_grpc import QrCodeServiceServicer, add_QrCodeServiceServicer_to_server


class SvgImageFactory(qrcode.image.svg.SvgPathImage):
    def __init__(self, *args, fill_color, back_color, **kwargs):
        self.background = back_color
        self.QR_PATH_STYLE = f'fill:{fill_color};fill-opacity:1;fill-rule:nonzero;stroke:none'
        super().__init__(*args, **kwargs)

    def _svg(self, **kwargs):
        svg = super()._svg(**kwargs)
        return svg


class QrCodeService(QrCodeServiceServicer):
    def CreateQrCode(self, request, context):
        version = request.version if 1 <= request.version <= 40 else None

        error_correction = {
            ErrorCorrection.LOW: qrcode.constants.ERROR_CORRECT_L,
            ErrorCorrection.MEDIUM: qrcode.constants.ERROR_CORRECT_M,
            ErrorCorrection.QUARTILE: qrcode.constants.ERROR_CORRECT_Q,
            ErrorCorrection.HIGH: qrcode.constants.ERROR_CORRECT_H,
        }[request.error_correction]

        image_factory = {
            ImageFormat.PNG: qrcode.image.pil.PilImage,
            ImageFormat.SVG: SvgImageFactory
        }[request.image_format]

        qr = qrcode.QRCode(version=version,
                           error_correction=error_correction,
                           box_size=request.box_size,
                           border=request.border,
                           image_factory=image_factory)
        qr.add_data(request.data)
        image = qr.make_image(fill_color=request.fill_color,
                              back_color=request.back_color)

        bytes_io = io.BytesIO()
        image.save(bytes_io)
        byte_array = bytes_io.getvalue()

        return QrCodeResponse(file=byte_array)


def serve():
    print("Starting server...")
    server = grpc.server(ThreadPoolExecutor(max_workers=10))
    add_QrCodeServiceServicer_to_server(QrCodeService(), server)
    server.add_insecure_port('[::]:5000')
    server.start()
    print("Listening on port 5000")
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
