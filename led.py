import RPi.GPIO as GPIO

from time import sleep, time

from threading import Thread

from vars import RED_LED, GREEN_LED

GPIO.setwarnings(False)

GPIO.setmode(GPIO.BOARD)

GPIO.setup(RED_LED, GPIO.OUT)
GPIO.setup(GREEN_LED, GPIO.OUT)

def led_loop():
    global time_passed

    time_passed = time()

    while True:
        if time() - time_passed > 0.2:
            GPIO.output(RED_LED, 0)

            GPIO.output(GREEN_LED, 0)

        sleep(0.1)

Thread(target = led_loop).start()

def record_time():
    global time_passed
    
    time_passed = time()