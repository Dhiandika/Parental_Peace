# main.py

from fastapi import FastAPI, File, UploadFile, HTTPException, Depends
# from fastapi.security.api_key import APIKeyHeader
from starlette.responses import JSONResponse
from predict import predict_audio_category
from tensorflow.keras.models import load_model
import json
import librosa
import uvicorn

app = FastAPI()

# Load the trained model
model_path = load_model(r'C:\Users\LENOVO\Documents\Iqbal\benkyo\fixbabymodel.h5')

# Declare api_key globally
# api_key = None
# API_KEY_NAME = "X-API-Key"
# api_key_header = APIKeyHeader(name=API_KEY_NAME, auto_error=False)

# async def get_api_key(api_key_header: str = Depends(api_key_header)):
#     if api_key_header == api_key:
#         return api_key_header
#     else:
#         raise HTTPException(
#             status_code=400, detail="Invalid API Key"
#         )

@app.post("/predict_audio")
async def predict_audio_route(file: UploadFile = File(...)):
    try:
        audio_file = await file.read()
        with open('uploaded_audio.wav', 'wb') as f:
            f.write(audio_file)

        # Assuming you have these defined elsewhere
        audio_data, sr = librosa.load('uploaded_audio.wav', sr=None)
        max_length = 10000  # Define the max_length

        prediksi_label = predict_audio_category(model_path, 'uploaded_audio.wav',  max_length)

        result = {'predicted_class': prediksi_label}
        return JSONResponse(content=result)
    except Exception as e:
        print(str(e))
        return JSONResponse(content={"status": "bad request"})

# Memulai Server
if __name__ == "__main__":
    # with open('./private/key.json', 'r') as fileKey:
    #     api_key = json.load(fileKey).get('api_key')

    print("Server: http://0.0.0.0:8080")
    uvicorn.run(app, host="0.0.0.0", port=8080)