import mfrc522

from playsound import playsound

from threading import Thread

from time import time

from led import GPIO, record_time

from vars import RED_LED, GREEN_LED

reader = mfrc522.SimpleMFRC522()

sound_done = 0

def nfc_loop():
    while True:
        id, text = reader.read()

        print(id, text)

        if id == 582238713219:
            GPIO.output(GREEN_LED, 1)

            GPIO.output(RED_LED, 0)

        else:
            GPIO.output(RED_LED, 1)

            GPIO.output(GREEN_LED, 0)

            global sound_done

            if sound_done == 0:
                Thread(target = pl_sound).start()

                sound_done = 1

        record_time()

Thread(target = nfc_loop).start()

def pl_sound():
    playsound('warning.mp3')

    global sound_done

    sound_done = 0