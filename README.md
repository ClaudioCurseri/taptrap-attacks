# TapTrap Attack Showcase

Android application showcasing different attacks using TapTrap.

TapTrap is a tapjacking attack targeting Android devices. The attack utilizes animations in order to secretly open another screen, such as a permission prompt, and make it invisible. This can be used to trick an user into performing sensitive actions, such as granting permissions, without the consent of the user.

The original paper can be found [here](https://taptrap.click/usenix25_taptrap_paper.pdf).

The official TapTrap Website can be found [here](https://taptrap.click).

The official TapTrap Git repository can be found [here](https://github.com/beerphilipp/taptrap).

---

## Types of attacks

This repository contains an Android app that showcases four different attack scenarios utilizing TapTrap. For more information, consult the sections below.

### Runtime Permission Bypass

The user believes he is interacting with a normal dialogue. In reality, without noticing, the user ends up granting the **camera permission**.

You can [read more about this type of attack](app/src/main/java/edu/hm/itsec/taptrapattackshowcase/runtimepermissions/README.md) and [watch a demo video](demo).

### Notification Listener Service Bypass

The user believes he is enabling the Notification Listener Service in the settings of the app. In reality, the Notification Listener Service is being enabled in the system settings, concealed from the user through a custom animation.

You can [read more about this type of attack](app/src/main/java/edu/hm/itsec/taptrapattackshowcase/notificationservice/README.md) and [watch a demo video](demo).

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