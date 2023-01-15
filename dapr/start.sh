dapr run --app-id auctionapi --app-port 8080 --dapr-http-port 3501 --components-path ./components -- java -jar ../target/*.jar
echo ""
echo "** PROGRAM CLOSED **"
