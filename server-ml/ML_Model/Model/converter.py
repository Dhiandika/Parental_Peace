from tensorflow.keras.models import load_model
import tensorflowjs as tfjs

model = load_model("babycrymodell.h5")
output_path = "tfjs"

tfjs.converters.save_keras_model(model, output_path)