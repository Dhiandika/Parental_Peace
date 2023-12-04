# main.py

from flask import Flask, request, jsonify
from waitress import serve
from predict import predict_audio_category
from tensorflow.keras.models import load_model
import json
import librosa


app = Flask(__name__)

    # Load the trained model
model_path = load_model('ML_Model/Model/babycrymodell.h5')


# Declare api_key globally
api_key = None

@app.route("/predict_audio", methods=['POST'])
def predict_audio_route():
    global api_key  # Use the global api_key variable
    if request.method == 'POST':
        # Mengambil Kunci API dari Request Header
        key = request.headers.get('X-API-Key')
        # Jika Kunci API Benar
        if key == api_key:
            # Try (jika request valid)
            try:
                audio_file = request.files['audio']
                audio_file.save('uploaded_audio.wav')

                # Assuming you have these defined elsewhere
                audio_data, sr = librosa.load('uploaded_audio.wav', sr=None)
                max_length = 10000 # Define the max_length

                prediksi_label = predict_audio_category(model_path, 'uploaded_audio.wav', max_length)

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
        api_key = json.load(fileKey).get('api_key')

    print("Server: http://0.0.0.0:8080")
    serve(app, host="0.0.0.0", port=8080)
