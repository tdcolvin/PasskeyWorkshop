# Android & passkeys workshop

## Back end Google Cloud / SimpleWebAuthn workshop

1. Install the Google Cloud CLI: [https://docs.cloud.google.com/sdk/docs/install-sdk](https://docs.cloud.google.com/sdk/docs/install-sdk)  
2. Install the Docker Engine: [https://docs.docker.com/engine/install/](https://docs.docker.com/engine/install/)  
3. Run gcloud auth login  
4. Run gcloud auth configure-docker [gcr.io](http://gcr.io) \- this configures your local Docker instance to use gcloud’s auth  
5. Create a Google Cloud project: [console.cloud.google.com](http://console.cloud.google.com), giving it the billing account you created using the link  
   ![](assets/create-project.png) 
6. Run docker build \-t gcr.io/\[YOUR PROJECT ID\]/passkey-auth-demo:latest .  
7. Run docker push gcr.io/\[YOUR PROJECT ID\]/passkey-auth-demo:latest  
   You’ll probably get a link to enabling the artifact registry API the first time. Enable the API.  
8. Deploy the container to Cloud Run:  
   gcloud run deploy passkey-auth-demo \--image gcr.io/test-project-for-passkeys/passkey-auth-demo:latest \--platform managed      \--region europe-west4 \--allow-unauthenticated \--project test-project-for-passkeys  
9. You’ll need a Firestore database. Follow the instructions here to enable the Firestore API and create a database: [https://console.cloud.google.com/firestore/databases](https://console.cloud.google.com/firestore/databases)![](assets/create-firestore.png)
10. After deployment, you’ll see a URL. Go to that URL to see your passkey server running nicely.  
11. Let’s try out the web interface. Enter a username, and hit Register:  
    ![](assets/user-register.png)
12. Uh oh\! ![](assets/user-register-error.png)
    Fix this by going to the Cloud Run console and deploying a new version of your service with an environment variable:  
    Name: RP\_ID  
    Value: your function’s domain name


