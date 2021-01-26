cd ../fyp-frontend
yarn build
rm -rf ../fyp-server/src/main/resources/build
cp -a build ../fyp-server/src/main/resources/
cd ../fyp-server
mvn clean install -DskipTests
heroku local web