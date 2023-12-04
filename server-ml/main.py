from flask import Flask, request, jsonify
from waitress import serve
from predict import predict_audio
import json

app = Flask(__name__)


@app.route("/predict_audio", methods=['POST'])
def predict_audio_route():
    if request.method == 'POST':
        # Mengambil Kunci API dari Request Header
        key = request.headers.get('X-API-Key')
        # Jika Kunci API Benar
        if key == api_key:
            # Try (jika request valid)
            try:
                audio_file = request.files['audio']
                audio_file.save('uploaded_audio.wav')

                prediksi_label = predict_audio('uploaded_audio.wav')

                result = {'predicted_class': prediksi_label}
                return jsonify(result)
            # catch (jika request tidak valid)
            except Exception as e:
                print(str(e))
                return jsonify({"status": "bad request"})
        # Jika Kunci API Salah
        else:
            return jsonify({"status": "unauthorized"})


# Memulai Server
if __name__ == "__main__":
    with open('./private/key.json', 'r') as fileKey:
        api_key = json.load(fileKey).get('key')

    print("Server: http://0.0.0.0:8080")
    serve(app, host="0.0.0.0", port=8080)
