# -*- coding: utf-8 -*-
from PIL import Image
import os, glob, numpy as np
from keras.models import load_model
from keras.applications.inception_v3 import preprocess_input
from keras.preprocessing import image
import keras.backend as K
import flask
import io
import os
import cv2
import shutil
from collections import Counter
import math
import time


# initialize our Flask application and the Keras model
app = flask.Flask(__name__)

image_w = 256
image_h = 256


# 학습한 모델 불러오기
def load_my_model():
    # data_dir = "./food-101/images/"
    # global foods_sorted
    # foods_sorted = sorted(os.listdir(data_dir))
    # print(foods_sorted)

    global foods_sorted
    # foods_sorted = ['apple_pie', 'baby_back_ribs', 'baklava', 'beef_carpaccio', 'beef_tartare', 'beet_salad', 'beignets', 'bibimbap', 'bread_pudding', 'breakfast_burrito', 'bruschetta', 'caesar_salad', 'cannoli', 'caprese_salad', 'carrot_cake', 'ceviche', 'cheese_plate', 'cheesecake', 'chicken_curry', 'chicken_quesadilla', 'chicken_wings', 'chocolate_cake', 'chocolate_mousse', 'churros', 'clam_chowder', 'club_sandwich', 'crab_cakes', 'creme_brulee', 'croque_madame', 'cup_cakes', 'deviled_eggs', 'donuts', 'dumplings', 'edamame', 'eggs_benedict', 'escargots', 'falafel', 'filet_mignon', 'fish_and_chips', 'foie_gras', 'french_fries', 'french_onion_soup', 'french_toast', 'fried_calamari', 'fried_rice', 'frozen_yogurt', 'garlic_bread', 'gnocchi', 'greek_salad', 'grilled_cheese_sandwich', 'grilled_salmon', 'guacamole', 'gyoza', 'hamburger', 'hot_and_sour_soup', 'hot_dog', 'huevos_rancheros', 'hummus', 'ice_cream', 'lasagna', 'lobster_bisque', 'lobster_roll_sandwich', 'macaroni_and_cheese', 'macarons', 'miso_soup', 'mussels', 'nachos', 'omelette', 'onion_rings', 'oysters', 'pad_thai', 'paella', 'pancakes', 'panna_cotta', 'peking_duck', 'pho', 'pizza', 'pork_chop', 'poutine', 'prime_rib', 'pulled_pork_sandwich', 'ramen', 'ravioli', 'red_velvet_cake', 'risotto', 'samosa', 'sashimi', 'scallops', 'seaweed_salad', 'shrimp_and_grits', 'spaghetti_bolognese', 'spaghetti_carbonara', 'spring_rolls', 'steak', 'strawberry_shortcake', 'sushi', 'tacos', 'takoyaki', 'tiramisu', 'tuna_tartare', 'waffles']
    foods_sorted = ['간장게장', '갈비구이', '갈비찜', '갈비탕', '감자탕', '곱창구이', '교자', '김치찌개', '김치찜', '까르보나라', '닭갈비', '닭계장', '닭볶음탕', '된장찌개', '라멘', '라면', '만두', '매운탕', '물냉면', '물회', '보쌈', '볶음밥', '불고기', '비빔냉면', '비빔밥', '삼겹살', '삼계탕', '설렁탕', '수육', '수제비', '순두부찌개', '스테이크', '스파게티', '양념게장', '양념치킨', '연어구이', '연어회', '오믈렛', '육개장', '육회', '잔치국수', '전골', '제육볶음', '조개구이', '족발', '주꾸미볶음', '짜장면', '짬뽕', '쫄면', '찜닭', '초밥', '추어탕', '카레', '칼국수', '콩국수', '파전', '피자', '해물찜', '햄버거', '후라이드치킨', '훈제오리']

    # Loading the saved model to make predictions
    K.clear_session()
    global model
    model = load_model('bestmodel_61class.hdf5', compile=False)
    print(" * InceptionV3 model loading is success")


# 이미지를 통한 음식 식별
@app.route("/predict", methods=["POST"])
def predict():
    # initialize the data dictionary that will be returned from the view
    response = {"success": False}

    # ensure an image was properly uploaded to our endpoint
    if flask.request.method == "POST":
        if flask.request.files.get("image"):
            # read the image in PIL format
            image = flask.request.files["image"].read()
            image = Image.open(io.BytesIO(image))
            image = image.convert("RGB")
            image = image.resize((image_w, image_h))

            image = np.expand_dims(image, axis=0)
            image = preprocess_input(image)

            pred = model.predict(image)
            index = np.argmax(pred)
            pred_value = foods_sorted[index]

            response["predictions"] = pred_value
            response["success"] = True

    # return the data dictionary as a JSON response
    return flask.jsonify(response)


# 동영상 경로에 저장
def save_video(video, folder_name):
    video_dir = './log/video/' + folder_name
    os.mkdir(video_dir)
    video_path = video_dir + '/' + video.filename
    video.save(video_path)
    return video_dir, video_path


# 동영상을 프레임 단위로 추출하여 저장
def video_to_images(video_path, folder_name):
    image_dir = './log/image/' + folder_name
    os.mkdir(image_dir)

    cap = cv2.VideoCapture(video_path)

    save_paths = []
    count = 0

    while True:
        ret, image = cap.read()
        if not ret:
            break

        count += 1
        if count % 10 != 0:
            continue

        fname = "{}.jpg".format("{0:05d}".format(count))
        save_path = image_dir + '/' + fname
        save_paths.append(save_path)
        cv2.imwrite(save_path, image)  # save frame as JPEG file
        # print(save_path)

    # print("{} images are extracted in {}.".format(count, './log/'))

    return image_dir, save_paths


# 이미지 리스트를 식별
def predict_class_multi(images):
    pred_values = []

    for img in images:
        img = image.load_img(img, target_size=(299, 299))
        img = image.img_to_array(img)
        img = np.expand_dims(img, axis=0)
        img = preprocess_input(img)

        pred = model.predict(img)
        index = np.argmax(pred)
        pred_value = foods_sorted[index]
        # print(pred_value)
        pred_values.append(pred_value)

    return pred_values


# 리스트에서 최빈값 구하기
def mode_finder(pred_values):
    c = Counter(pred_values)
    mode = c.most_common(1)
    return mode[0][0]


## 리스트 중복 제거하기
def duplicate_filter(pred_values):
    pred_set = set(pred_values)  # 집합set으로 변환
    new_list = list(pred_set)
    return new_list


# 동영상을 통한 음식 식별
@app.route("/video", methods=["POST"])
def predict_video():
    # initialize the data dictionary that will be returned from the view
    response = {"success": False}

    video = flask.request.files['video']
    seed = str(math.ceil(time.time()))
    folder_name = seed + video.filename[:-4]
    video_dir, video_path = save_video(video, folder_name)
    image_dir, save_paths = video_to_images(video_path, folder_name)
    pred_values = predict_class_multi(save_paths)
    pred_value = mode_finder(pred_values)

    # 사용이 끝난 리소스는 삭제
    shutil.rmtree(video_dir)
    shutil.rmtree(image_dir)

    response["predictions"] = pred_value
    response["success"] = True

    # return the data dictionary as a JSON response
    return flask.jsonify(response)


# 동영상을 통한 음식 식별
@app.route("/video/list", methods=["POST"])
def predict_video_list():
    # initialize the data dictionary that will be returned from the view
    response = {"success": False}

    video = flask.request.files['video']
    seed = str(math.ceil(time.time()))
    folder_name = seed + video.filename[:-4]
    video_dir, video_path = save_video(video, folder_name)
    image_dir, save_paths = video_to_images(video_path, folder_name)
    pred_values = predict_class_multi(save_paths)
    new_list = duplicate_filter(pred_values)

    # 사용이 끝난 리소스는 삭제
    shutil.rmtree(video_dir)
    shutil.rmtree(image_dir)

    response["predictions"] = new_list
    response["success"] = True

    # return the data dictionary as a JSON response
    return flask.jsonify(response)


# 메인함수
if __name__ == '__main__':
    print((" * Loading Keras InceptionV3 model and Flask starting server..."
           "please wait until server has fully started"))
    load_my_model()
    app.run(host='0.0.0.0')