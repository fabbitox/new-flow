from flask import Flask

app = Flask(__name__)

@app.route('/favicon.ico')
def favicon():
    return None

@app.route('/predict/<int:id>')
def predict(id):
    if id == 1:
        return 'predict sinpyeong'
    else:
        return 'not found'

if __name__ == '__main__':
    app.run()