# remider-IoT
A individual project

Raspberry pi is a minicomputer, hence it has ports which need to be wiring of the project. Powering source comes from the PIR sensor, that is, only the sensor is powered, and it relays the power to the other devices in the connections like the raspberry module and buzzer. Starting from the PIR sensor, positive terminal is connected to pin 4 of raspberry pi, ground to pin 6, and the third terminal to pin 21,26 for the LED and buzzer output pin. 
The project responds positively when powered on. Several pins of the raspberry pi module make it possible to integrate several devices according to the objective. The following diagram has been used for illustration. The flow diagram shows how the PIR sensor behaves when there is an intruder and no intruder. It registers a 1 and 0 when there is an intruder and not respectively. The PIR sensor used is a wired sensor of 5V DC. Figure 1 link below illustrates the system flow diagram.

https://upload.cc/i1/2018/08/18/dEOzoR.jpg

Figure 1

Where GPIO is allocated to corresponding devices for Pin 4, 21 and 26 is set for motion sensor, LED and buzzer respectively. The motion sensor input pin 4 is allocated for sensing motion, output pin 21 and 26 for LED and buzzer. When GPIO state is high (GPIO=True) and codes activated, give outputs accordingly. 

GPIO states, with reference to PIR codes of appendix 1, enables the various hardware components to function. Access to various modules is provided by GPIO and is dependent on its state. 

During 'GPIO=0' state or low state, the sensor is at rest (off). There is no output from the sensor and it remains at 'rest' state if nothing is detected. 
Motion sensor gets turned on, when there is possible intrusion (high state 'GPIO 4=True'). The buzzer and timer is simultaneously activated.
 
The program stops for 2 seconds to perform GPIO reset to avoid false alarms and re-starts. While stopping that it would then wait serversocket.py for sending data to control alarm off from the client of android APP. The program developed keeps running and waiting an interrupt comes in the detection range of the sensor, the process repeats. Figure 2 is the demonstration of reminder.apk

https://upload.cc/i1/2018/08/18/UrtAi2.png

Figure 2


Andoird APP as figure 3 below:
https://upload.cc/i1/2018/08/21/IgYCxB.png

