echo "🌀🌀🌀Deploying StealthCannabis Backend 🌀🌀🌀"
echo "🌀🌀🌀Creating container and pushing it to GCP registry 🌀🌀🌀"

./mvnw compile com.google.cloud.tools:jib-maven-plugin:3.3.1:build \
  -Dimage=gcr.io/stealthcannabis/stealthapp

echo "🍎🍎🍎Deploy newly created Kasie Transie container to Cloud Run 🍎🍎🍎"
gcloud run deploy stealthapp \
     --region=us-west1 \
     --platform=managed \
     --project=stealthcannabis \
     --allow-unauthenticated \
     --update-env-vars "GOOGLE_CLOUD_PROJECT=stealthcannabis, PROJECT_ID=stealthcannabis" \
     --image=gcr.io/stealthcannabis/stealthapp

echo "🍎🍎🍎 ... hopefully deployed StealthCannabis on Cloud Run 🍎🍎🍎"