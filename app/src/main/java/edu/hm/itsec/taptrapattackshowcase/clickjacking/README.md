# Clickjacking Attack with TapTrap

## What is it?

In this demo, the user believes he is interacting with a normal Android app. In reality, he is interacting with an embedded web page.  
Using **CustomTabs** combined with **custom animations**, the web page is visually hidden from the user, enabling a clickjacking attack.

---

## Demo Scenario

The user selects a book and views its details — or so he thinks. In reality, his taps are redirected to a webshop where he unknowingly buys a product.

The demonstration consists of two activities:

### BookSelectionActivity

- When this activity starts, it immediately launches a **CustomTab** that loads the webshop's `/shop` page.
- A **custom animation** hides the CustomTab from the user.
- The user taps the **"View Details"** button, but the tap is actually registered on the webshop’s **"Add to Cart"** button.

### BookDetailsActivity

- This activity displays the details of the selected book.
- When it starts, it launches a **CustomTab** containing the webshop’s `/cart` page, again hidden using a custom animation.
- The user taps the **"Start Reading"** button, but this tap triggers the purchase of all products in cart on the webpage.

### Animation Timing

Android limits animation duration to 6 seconds. After those 6 seconds, the CustomTab would normally become visible.  
To keep it hidden, the activity is automatically restarted when the animation ends.

---

## Configuration

### Demo Website

The demo website must be running for the attack to work. You can find more information about setting it up [**here**](../../../../../../../../../web-attacks-demo-site/README.md).  
Set the website URL in [**Constants.kt**](../Constants.kt).

Before starting the demo scenario, open the website in your browser and allow ngrok to visit the website.

### Browser

Not all browsers support TapTrap. For example, **Chrome** blocks user input while animations are running, which prevents the demo from working as intended.
To try the demo, set **Samsung Internet** as your default browser.

### Transparency

For analyzing the attack, you can disable the transparency of the CustomTab.  
Use the toggle switch before starting the demo scenario to observe how the attack works.
