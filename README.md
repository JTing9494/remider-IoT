# Reminder-IoT

A simple Raspberry Pi–based intrusion-detection and reminder system. When motion is detected by a PIR sensor, the system triggers both an LED and a buzzer and logs the event. Optionally, it can send data to a client (e.g., an Android app) to allow remote “alarm off” control and record events in SQLite or Firebase.

---

## Table of Contents

1. [Project Overview](#project-overview)  
2. [Hardware Components](#hardware-components)  
3. [Wiring and GPIO Mapping](#wiring-and-gpio-mapping)  
4. [System Flow](#system-flow)  
5. [Software Setup](#software-setup)  
   1. [Prerequisites](#prerequisites)  
   2. [server_socket.py Overview](#server_socketpy-overview)  
   3. [SQLite and Firebase Logging](#sqlite-and-firebase-logging)  
6. [Running the System](#running-the-system)  
7. [Android App (Reminder.apk)](#android-app-reminderapk)  
8. [References](#references)  

---

## 1. Project Overview

This “Reminder-IoT” project uses a Raspberry Pi and a PIR (Passive Infrared) motion sensor to detect movement (e.g., an intruder). When motion is detected:

- A buzzer sounds.  
- An LED lights up.  
- (Optionally) A small Python server (`server_socket.py`) runs on the Pi, allowing an Android client to remotely turn off the alarm.  
- Event logs are stored locally in SQLite and can also be synced to Firebase in real time.

The goal is to illustrate a minimal­-footprint “smart” alarm system built around off-the-shelf Raspberry Pi GPIO peripherals.

---

## 2. Hardware Components

- **Raspberry Pi** (any model with 5 V GPIO; this example uses a Pi 3)
- **PIR Motion Sensor** (5 V DC; wired)  
- **LED** (e.g., 5 mm, any color)  
- **Active Buzzer** (5 V)  
- **Jumper Wires**  
- **Breadboard** (optional, for prototyping)  
- **Micro USB Power Supply** (5 V, ≥2 A for Pi + peripherals)

### Pinouts (Raspberry Pi GPIO)

| Device       | Pi GPIO Pin | Broadcom (BCM) Number | Function     |
|:-------------|:------------|:----------------------|:-------------|
| PIR VCC      | Pin 4       |  (5 V)                 | Power input  |
| PIR GND      | Pin 6       |  (GND)                 | Sensor ground|
| PIR OUT      | Pin 21      | (BCM 9)                | Motion output |
| LED (positive) | Pin 26    | (BCM 7)                | LED output   |
| Buzzer (positive) | Pin 26 or any free GPIO | (reuse BCM 7) | Buzzer output (shared with LED or separate pin as desired) |
| LED/Buzzer GND | Pi GND     | (e.g., Pin 14 or Pin 39) | Common ground |

> **Note:** In the table above, Pin 21 (BCM 9) is the PIR signal output. Pin 26 (BCM 7) can drive both an LED and a buzzer via proper current-limiting resistors or transistor switches. If you prefer separate GPIOs for LED and buzzer, simply choose a different free BCM pin (e.g., BCM 11 or BCM 10) and update the code accordingly.

---

## 3. Wiring and GPIO Mapping

1. **Power the PIR sensor**  
   - Connect the PIR’s **VCC** pin to Pi’s **Pin 4 (5 V)**.  
   - Connect the PIR’s **GND** pin to Pi’s **Pin 6 (GND)**.

2. **Read PIR output**  
   - Connect the PIR’s **OUT** pin to Pi’s **Pin 21** (BCM 9).

3. **Drive LED and Buzzer**  
   - Connect LED **positive ( + )** pin (via a suitable resistor, e.g., 220 Ω) to Pi’s **Pin 26** (BCM 7).  
     - LED negative (–) leg → Pi’s GND (e.g., Pin 39).  
   - Connect buzzer **positive ( + )** pin (if it’s an active buzzer) also to Pi’s **Pin 26** (BCM 7) or to a second GPIO if you want to sequence them separately.  
     - Buzzer negative (–) leg → Pi’s GND (common ground).

> **Important:** If you drive both LED and buzzer from the same GPIO, ensure the total current draw does not exceed the Pi’s per-GPIO limit (~16 mA). Otherwise, use a transistor (NPN + base resistor) or a dedicated driver board.

<img src="https://upload.cc/i1/2018/08/18/dEOzoR.jpg" alt="Figure 1: System Flow Diagram" width="400"/>

> **Figure 1:** System flow diagram. PIR sensor outputs HIGH (1) when motion is detected and LOW (0) otherwise. The Pi reads this and triggers the LED/buzzer, waits 2 s (debounce), then resumes listening.

---

## 4. System Flow

1. **Idle State (GPIO LOW)**  
   - PIR is at rest. No motion detected.  
   - LED is off, buzzer is silent.  

2. **Motion Detected (GPIO HIGH on Pi BCM 9)**  
   - Pi reads a `1` from PIR → enters alarm state.  
   - Turn **LED ON** (GPIO HIGH on BCM 7).  
   - Sound **Buzzer** (also GPIO HIGH on BCM 7).  
   - **Log event** (timestamp + “motion detected”) to local SQLite (and optionally Firebase).  
   - Start a **2 s delay** (to avoid false retriggers).  
   - Launch (or continue) `server_socket.py` to open a simple TCP socket, listening for a remote “alarm off” command from an Android client.  
   - Meanwhile, continue scanning PIR; if still HIGH, remain in alarm state until “alarm off” is received.

3. **Alarm Off**  
   - Android client (Reminder.apk) connects to Pi’s IP via socket, sends a “stop” command.  
   - Pi receives “alarm off” → turn **LED OFF** and **Buzzer OFF** (GPIO LOW on BCM 7).  
   - Log “alarm turned off” event.  
   - Return to **Idle State** (listen for GPIO LOW → HIGH transitions from PIR).

---

