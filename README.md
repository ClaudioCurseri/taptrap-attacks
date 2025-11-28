# taptrap-attacks

Android Application showcasing different attacks using TapTrap.

---

## Types of attacks

### Runtime Permission Bypass

### Notification Listener Service Bypass

### Web Permission Bypass

The user believes he is playing a simple game where he has to tap the correct button.  
In reality, without noticing, the user ends up granting **geolocation permission** to a web page loaded inside a hidden **CustomTab**.  
The CustomTab is concealed from the user through a custom animation.

You can [read more about this type of attack](app/src/main/java/edu/hm/itsec/taptrapattackshowcase/web/README.md) and [watch a demo video](demo).

### Clickjacking

The user believes he is interacting with a normal Android app. In reality, he is interacting with an embedded web page.  
Using **CustomTabs** combined with **custom animations**, the web page is visually hidden from the user, enabling a clickjacking attack.

You can [read more about this type of attack](app/src/main/java/edu/hm/itsec/taptrapattackshowcase/clickjacking/README.md) and [watch a demo video](demo).

---

## Configuration