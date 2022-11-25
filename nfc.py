import mfrc522

from threading import Thread

from time import time

from led import GPIO, record_time

from vars import RED_LED, GREEN_LED

reader = mfrc522.SimpleMFRC522()

def nfc_loop():
    while True:
        id, text = reader.read()

        # print(id)

        if id == 582238713219:
            GPIO.output(GREEN_LED, 1)

        else:
            GPIO.output(RED_LED, 1)

        record_time()

Thread(target = nfc_loop).start()