# Web Permission Bypass Attack with TapTrap

## What is it?

In this demo, the user believes he is playing a simple game where he has to tap the correct button.  
In reality, without noticing, the user ends up granting **geolocation permission** to a web page loaded inside a hidden **CustomTab**.  
The CustomTab is concealed from the user through a custom animation.

---

## Demo Scenario

While playing the game, the user taps buttons to increase the score.  
Once the score reaches **2**, a **CustomTab** is opened in the background, hidden by a custom animation.  
The embedded web page requests the **geolocation web permission**.

The user continues tapping what appears to be a game button — but in fact, the tap is applied to the hidden permission prompt, unintentionally granting geolocation access.

### Animation Timing

Android limits animation duration to 6 seconds. After those 6 seconds, the CustomTab would normally become visible.  
To keep it hidden, the activity is automatically restarted when the animation ends.

---

## Configuration

### Demo Website

The demo website must be running for the attack to work. You can find more information about setting it up [**here**](../../../../../../../../../web-attacks-demo-site/README.md).  
Set the website URL in [**Constants.kt**](../Constants.kt).

Before starting the demo game, open the website in your browser and allow ngrok to visit the website. You will see
the web permission dialog. Do not grant the permission, just close the browser and proceed in the app.

### Browser

Not all browsers support TapTrap. For example, **Chrome** blocks user input while animations are running, which prevents the demo from working as intended.
To try the demo, set **Samsung Internet** as your default browser.

### Transparency

For analyzing the attack, you can disable the transparency of the CustomTab.  
Use the toggle switch before starting the demo scenario to observe how the attack works.
