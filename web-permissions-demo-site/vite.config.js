import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    https: false,
    host: false, 
    allowedHosts: [
        // add your ngrok URL here
    ]
  },
});