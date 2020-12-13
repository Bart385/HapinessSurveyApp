import pandas as pd
import tensorflow as tf
from tensorflow import keras

df = pd.read_csv("data/survey.csv", sep=',')

questions = {
    "X1": "the availability of information about the city services",
    "X2": "the cost of housing",
    "X3": "the overall quality of public schools",
    "X4": "your trust in the local police",
    "X5": "the maintenance of streets and sidewalks",
    "X6": "the availability of social community events"
}

es = keras.callbacks.EarlyStopping(monitor='val_loss', mode='min', verbose=1, patience=5)
mc = keras.callbacks.ModelCheckpoint('./models/best_model.h5', monitor='val_loss', mode='min', save_best_only=True,
                     save_weights_only=False)

X = df[list(questions.keys())].astype(float)
Y = df['D'].astype(float)


model = tf.keras.models.Sequential([
    tf.keras.layers.Flatten(input_shape=(6, 1)),
    tf.keras.layers.Dense(24, activation='tanh'),
    tf.keras.layers.Dense(1, activation='sigmoid')
])

model.compile(
    optimizer=tf.keras.optimizers.Adam(0.0001),
    loss='binary_crossentropy',
    metrics=['accuracy'],
)

model.fit(
    x=X,
    y=Y,
    epochs=1000,
    shuffle=True,
    validation_split=0.3,
    callbacks=[mc]
)

converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# Save the model.
with open('model.tflite', 'wb') as f:
  f.write(tflite_model)


