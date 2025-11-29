# Runtime Permission Bypass Attack with TapTrap

## What is it?

In this demo, the user believes he is interacting with a normal dialogue. In reality, without noticing, the user ends up granting the **camera permission**.

---

## Demo Scenario

Upon starting the demo, a dialogue containing some friendly advice will be shown to the user. At the same time, the dialogue requesting the camera permission will also be active, concealed from the user with a custom animation.

### Animation Timing

Android limits the animation duration to 6 seconds. After those 6 seconds, the dialogue would normally become visible.  
To keep it hidden, the activity is automatically restarted when the animation ends.

---

## Configuration

### Camera Permission

Make sure that the camera permission is not currently granted if you want to observe the attack. Otherwise the selfie camera will just immediately be shown to you.

### Transparency

For analyzing the attack, you can disable the transparency of the dialogue.  
Use the toggle switch before starting the demo scenario to observe how the attack works.
