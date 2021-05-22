# from __future__ import absolute_import, division, print_function
#
# import tensorflow as tf
#
import tensorflow.keras.backend as K
# from tensorflow.keras.models import load_model
# from tensorflow.keras.preprocessing import image
from tensorflow.keras import regularizers
from tensorflow.keras.applications.inception_v3 import InceptionV3
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Dense, Dropout
from tensorflow.keras.layers import GlobalAveragePooling2D
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.callbacks import ModelCheckpoint, CSVLogger
from tensorflow.keras.optimizers import SGD
# from tensorflow.keras.regularizers import l2
#
# from tensorflow import keras
# from tensorflow.keras import models
from tensorflow.keras.applications.inception_v3 import preprocess_input
#
# import cv2
import os
# import random
# import collections
# from collections import defaultdict
#
# from shutil import copy
# from shutil import copytree, rmtree
#
# import numpy as np
#
# import matplotlib.pyplot as plt
# import matplotlib.image as img


def train_model(n_classes,num_epochs, nb_train_samples,nb_validation_samples):
  K.clear_session()

  img_width, img_height = 299, 299
  train_data_dir = 'food/train'
  validation_data_dir = 'food/test'
  batch_size = 16
  bestmodel_path = 'bestmodel_'+str(n_classes)+'class.hdf5'
  trainedmodel_path = 'trainedmodel_'+str(n_classes)+'class.hdf5'
  history_path = 'history_'+str(n_classes)+'.log'

  train_datagen = ImageDataGenerator(
      preprocessing_function=preprocess_input,
      shear_range=0.2,
      zoom_range=0.2,
      horizontal_flip=True)

  test_datagen = ImageDataGenerator(preprocessing_function=preprocess_input)

  train_generator = train_datagen.flow_from_directory(
      train_data_dir,
      target_size=(img_height, img_width),
      batch_size=batch_size,
      class_mode='categorical')

  validation_generator = test_datagen.flow_from_directory(
      validation_data_dir,
      target_size=(img_height, img_width),
      batch_size=batch_size,
      class_mode='categorical')


  inception = InceptionV3(weights='imagenet', include_top=False)
  x = inception.output
  x = GlobalAveragePooling2D()(x)
  x = Dense(128,activation='relu')(x)
  x = Dropout(0.2)(x)

  predictions = Dense(n_classes,kernel_regularizer=regularizers.l2(0.005), activation='softmax')(x)

  model = Model(inputs=inception.input, outputs=predictions)
  model.compile(optimizer=SGD(lr=0.0001, momentum=0.9), loss='categorical_crossentropy', metrics=['accuracy'])
  checkpoint = ModelCheckpoint(filepath=bestmodel_path, verbose=1, save_best_only=True)
  csv_logger = CSVLogger(history_path)

  history = model.fit(train_generator,
                      steps_per_epoch = nb_train_samples // batch_size,
                      validation_data=validation_generator,
                      validation_steps=nb_validation_samples // batch_size,
                      epochs=num_epochs,
                      verbose=1,
                      callbacks=[csv_logger, checkpoint])

  model.save(trainedmodel_path)
  class_map = train_generator.class_indices
  return history, class_map


def do_train(train_files, test_files, foods_sorted):
    # Train the model with data from all(101) classes
    n_classes = len(foods_sorted)
    epochs = 20
    nb_train_samples = train_files
    nb_validation_samples = test_files

    history, class_map = train_model(n_classes, epochs, nb_train_samples, nb_validation_samples)
    print(class_map)


# 메인함수
if __name__ == '__main__':
    print("start training...")

    foods_sorted = sorted(os.listdir('food/train'))
    train_files = sum([len(files) for i, j, files in os.walk("food/train")])
    test_files = sum([len(files) for i, j, files in os.walk("food/test")])

    do_train(train_files, test_files, foods_sorted)