# Notification Listener Service Bypass Attack with TapTrap

**Important:** This attack has been developed and tested on a Samsung Galaxy A54. Also, during the attack the app has to be selected from a list of all installed apps on the device that are allowed to implement a Notification Listener Service. This means that the app might not be located on the same spot in the list as the switch in the app. 

## What is it?

In this demo, the user believes he is enabling the Notification Listener Service in the settings of the app. In reality, the Notification Listener Service is being enabled in the system settings, concealed from the user through a custom animation.

---

## Demo Scenario

Upon starting the demo, a settings page with a single setting and a confirm button will appear. The components displayed on the page are not interactive, because the system settings are on top of the activity at the same time. 
Because the Notification Listener Service is required to be first enabled and then confirmed in the system settings, the user has to tap on two different locations on the screen. Since the first tap cannot be registered by the app, it will always be predicted that the user activates the switch after **3 seconds**. The second tap has to be placed on the confirm button, because that is where the confirmation dialogue will appear in the system settings.
Once the user has granted the permission, the newest notification that is currently in the notifications list on the device will be displayed live on the screen.

### Animation Timing

Android limits the animation duration to 6 seconds. After those 6 seconds, the settings page would normally become visible.  
To keep it hidden, the activity is automatically restarted when the animation ends.

---

## Configuration

### Notification Listener Service Permission

Make sure that the permission is not currently granted if you want to observe the attack.

### Transparency

For analyzing the attack, you can disable the transparency of the settings page.  
Use the toggle switch before starting the demo scenario to observe how the attack works.
