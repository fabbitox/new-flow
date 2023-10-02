from flask import Flask, request, jsonify
import numpy as np
from tensorflow.keras.models import load_model

app = Flask(__name__)

@app.route('/favicon.ico')
def favicon():
    return None

def preprocess(data):
    fcst_values = data['fcstValues']
    for sequence in fcst_values:
        for values in sequence:
            if values[3] == '강수없음':
                values[3] = '0'
    water_levels = data['waterLevels']
    water = [water_levels[i:i+5] for i in range(3)]
    return np.array(fcst_values, dtype=float)[:, np.newaxis], np.array(water, dtype=float)[np.newaxis, :]

@app.route('/predict/<int:id>', methods=['POST'])
def predict(id):
    if id == 1:
        print(request)
        aws, water = preprocess(request.json)
        model = load_model('model/model_1000.h5')
        return jsonify(model.predict([*aws, water]).tolist())
    else:
        return 'not found'

if __name__ == '__main__':
    app.run()
