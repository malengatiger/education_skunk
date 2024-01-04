echo "🌀🌀🌀Deploying Skunkworks Backend 🌀🌀🌀"
echo "🌀🌀🌀Creating container and pushing it to GCP registry 🌀🌀🌀"

./mvnw compile com.google.cloud.tools:jib-maven-plugin:3.3.1:build \
  -Dimage=gcr.io/skunkworks-ai/skunkworks-service

echo "🍎🍎🍎Deploy newly created Skunkworks Backend Service container to Cloud Run 🍎🍎🍎"
gcloud run deploy skunkworks-backend-service \
     --region=europe-west2 \
     --platform=managed \
     --project=skunkworks-ai \
     --allow-unauthenticated \
     --update-env-vars "GOOGLE_CLOUD_PROJECT=skunkworks-ai, PROJECT_ID=skunkworks-ai" \
     --image=gcr.io/skunkworks-ai/skunkworks-service

echo "🍎🍎🍎 ... hopefully deployed Skunkworks Backend Service on Cloud Run 🍎🍎🍎"

