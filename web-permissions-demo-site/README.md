# Web Permissions Demo

This site is a demo site for demonstration of granting web permissions with TapTrap.
If the geolocation permission was successfully granted, the location is displayed on the website.

## Starting the website

```bash
npm install
npm run dev

ngrok config add-authtoken <your-auth-token>
npx ngrok http <port-of-running-demo-site>
```

Run `npm install` to install required packages. Then start the site with `npm run dev`.

In order to access the site from a device that is not your computer with https connection, start ngrok with `npx ngrok http <port>`. You have to [create an ngrok account](https://dashboard.ngrok.com/login) and login to your account at first.

### Important
Your generated ngrok URL has to be allowed in the `vite.config.js` file.