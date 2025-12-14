# Android jet-fighter(as a car) Game – HW1

This project is a simple Android game created as part of the first homework assignment.

The game simulates a F-35 flying on a three-lane sky.  
The player can move the jet-fighter left and right in order to avoid birds that appear in the sky.

---

## Game Description
- The sky has three lanes.
- The jet-fighter can move left and right using buttons.
- Obstacles move down the screen at a constant speed.
- When the jet-fighter hits an obstacle, the player loses one life.
- The player starts with 3 lives.
- A toast message and vibration are shown on a crash.
- When all lives are lost, the game restarts and continues endlessly.

---

## Implementation
- The app is written in **Kotlin**.
- The layout is built using **ConstraintLayout**.
- Game objects are regular Android Views (ImageView).
- Movement is handled using a timer/handler.
- Collision is detected by checking overlapping views.

---

## How to Run
1. Open the project in Android Studio.
2. Build and run the app on an emulator or Android device.
3. Use the left and right buttons to control the jet-fighter.

---

## Notes
- The project does not use Canvas.
- The game speed is constant.
- The goal of the project is to practice basic Android concepts.

---

## Author
Student Name: ________
