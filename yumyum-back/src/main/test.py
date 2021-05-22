# -*- coding: utf-8 -*-
from PIL import Image
import os, glob, numpy as np
from keras.models import load_model
import flask
import io


app = flask.Flask(__name__)


@app.route("/test", methods=["GET"])
def predict():
    # initialize the data dictionary that will be returned from the view
    response = {"success": False}

    response["message"] = "hello"

    # return the data dictionary as a JSON response
    return flask.jsonify(response)


def jython_predict(a, b):
    c = a + b
    return c


# 메인함수
if __name__ == '__main__':
    app.run(host='0.0.0.0')