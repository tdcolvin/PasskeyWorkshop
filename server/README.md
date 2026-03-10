# Passkey demo app on Android

This app demonstrates a full end-to-end passkey implementation with a NodeJS server and Javascript front-end, using [SimpleWebAuthn](https://simplewebauthn.dev/). The code is an express application.

This also works with the Android implementation at https://github.com/tdcolvin/PasskeyAuthDemoAndroid.

This code is also running here: auth.tomcolvin.co.uk.

---

## Running on Cloud Run 🚀

The Firebase Functions version has been replaced with a standalone Express
application containerised using the `Dockerfile` in the project root. The same
routes and Realtime Database logic continue to work; Cloud Run simply provides
an HTTP server instead of a Functions trigger.

### Build & deploy steps

1. **Build the container** from the repository root:
   ```bash
   docker build -t gcr.io/$(gcloud config get-value project)/passkey-auth-demo:latest .
   ```
2. **Push to Container Registry** (or Artifact Registry):
   ```bash
   docker push gcr.io/$(gcloud config get-value project)/passkey-auth-demo:latest
   ```
3. **Deploy to Cloud Run**:
   ```bash
   gcloud run deploy passkey-auth-demo \
     --image gcr.io/$(gcloud config get-value project)/passkey-auth-demo:latest \
     --platform managed \
     --region us-central1 \
     --allow-unauthenticated
   ```

   - Set `--region` to the desired region.
   - Cloud Run will automatically set `PORT=8080` inside the container.

4. **Environment variables** such as `RP_ID` or `ENABLE_CONFORMANCE` can be
   supplied through `gcloud run deploy --set-env-vars` or via the Cloud Run
   console. The service account attached to the Cloud Run service must have
   access to your Firebase Realtime Database; the default Compute/Cloud Run
   service account normally already does.

> The `GOOGLE_APPLICATION_CREDENTIALS` variable is **not required** when running
> on Cloud Run: the Admin SDK will use the workload identity credentials
> provided by the platform.

### Local development

To iterate locally you can still use the same TypeScript sources from the
project root:

```bash
npm install    # first time
npm run build   # compile to lib/
npm start       # start the server with Node
# or for the very fastest feedback:
npm run dev     # runs ts-node directly
```

Static assets under `public/` are served automatically by the Express app.

---

Please open an issue if you hit any problems. And if you need help implementing passkeys in your own app then I'm [available as a freelancer](https://www.tomcolvin.co.uk) 😁. 
Contact me on [LinkedIn](https://www.linkedin.com/in/tdcolvin/) or [Bluesky](https://bsky.app/profile/tomcolvin.co.uk).
