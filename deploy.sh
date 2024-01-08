echo "🌀🌀🌀Deploying Skunkworks Backend 🌀🌀🌀"
echo "🌀🌀🌀Creating container and pushing it to GCP registry 🌀🌀🌀"

./mvnw compile com.google.cloud.tools:jib-maven-plugin:3.3.1:build \
  -Dimage=gcr.io/sgela-ai-33/sgela-backend

echo "🍎🍎🍎Deploy newly created Sgela AI Backend Service container to Cloud Run 🍎🍎🍎"
gcloud run deploy sgela-backend \
     --region=us-east4 \
     --platform=managed \
     --project=sgela-ai-33 \
     --allow-unauthenticated \
     --update-env-vars "GOOGLE_CLOUD_PROJECT=sgela-ai-33, PROJECT_ID=sgela-ai-33" \
     --image=gcr.io/sgela-ai-33/sgela-backend

echo "🍎🍎🍎 ... hopefully deployed SgelaAI Backend Service on Cloud Run 🍎🍎🍎"

