# PumpPals

PumpPals is a fitness social media application that allows users to meet, connect, follow, post, and log workouts and progress. The application is built using React for the frontend and Spring Boot for the backend. The database used is MongoDB, and user files are stored in an Amazon S3 bucket.

## Prerequisites

Before running the application, make sure you have the following:

- Clone the repository to your local machine.
- Create a `.env` file in `src/main/resources/` and fill in the required parameters in `application.properties`.
- Set up a working MongoDB cluster.
- Create an Amazon S3 bucket and an IAM service worker with the necessary permissions.

## Documentation

For detailed documentation on this project, please refer to the [PumpPals Documentation](https://mliamsinclair.dev/assets/PumpPalsDocumentation-oQJvCSsV.pdf).

## Running the Application

Once you have met all the prerequisites, follow these steps to run the application:

1. Build the project into an executable JAR file using the command `mvn clean install`.
2. Run the generated JAR file: java -jar target/pumppals-api-3.2.0.jar.
3. Access the application in your web browser at http://localhost:8080.

Please note that running the application locally creates a server instance. Any data uploaded will be stored in the cloud Amazon S3 bucket and MongoDB cluster, ensuring that no data is lost if the local server is stopped.