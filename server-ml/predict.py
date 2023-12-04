import os
import librosa
import numpy as np
from tensorflow import keras
from sklearn.preprocessing import LabelEncoder

# Inisialisasi list untuk menyimpan fitur audio dan label
audio_features = []

# Load your pre-trained model
model = keras.models.load_model('./server-ml/ML_Model/Model/babycrymodel (1).h5')

# Load label encoder
label_encoder = LabelEncoder()
label_encoder.classes_ = np.load('/content/drive/MyDrive/Capstone Project ML/label_encoder_classes.npy')

def predict_audio(audio_path):
    global model, label_encoder

    # Membaca file audio
    audio, sr = librosa.load(audio_path, sr=None)

    # Ekstraksi fitur audio (misalnya, MFCC)
    mfccs = librosa.feature.mfcc(y=audio, sr=sr, n_mfcc=13)

    # Zero-padding untuk menyamakan panjang fitur
    max_length = 10000  # Sesuaikan panjang sesuai kebutuhan Anda
    if mfccs.shape[1] < max_length:
        pad_width = max_length - mfccs.shape[1]
        mfccs = np.pad(mfccs, pad_width=((0, 0), (0, pad_width)), mode='constant')
    else:
        mfccs = mfccs[:, :max_length]

    # Menyesuaikan dimensi untuk memprediksi
    mfccs = np.expand_dims(mfccs, axis=0)

    # Melakukan prediksi menggunakan model
    prediksi = model.predict(mfccs)
    predicted_class = np.argmax(prediksi)
    prediksi_label = label_encoder.classes_[predicted_class]

    return prediksi_label
