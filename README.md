# clojure-lambda-db
Base Clojure Lambda Project with Database pooling for efficient performance


Install the [AWS-CLI](https://aws.amazon.com/cli) for deployment

```
brew install awscli
```

### Build

```
lein uberjar
```

### Application Logic
```

The core framework handles parsing of request to clojure map and formatting of responses. All logic goes into aws.lambda.helloWorld.clj in the handle-request function. Request comes as a parsed clojure map in request-map

(defn handle-request [request-map]
  (let [sz-input (prn-str request-map)]
  (do
    (println "Process Request"))
  ;; some response
  {:return 0
   :input sz-input
   :message "Hello World"}))
```

### Deploy

```
aws lambda create-function --runtime java8 --memory-size 512 --timeout 10 --description " some description of the function " --function-name clojure-hello-world --handler  aws.lambda.helloWorld --role "arn:aws:iam::873150696559:role/lambda_basic_execution" --zip-file fileb://./target/aws-lambda-hello-world-0.1.0-standalone.jar --profile GR_GG_COF_AWS_Sandbox_Dev_Developer

or

Open AWS Lambda GUI -> Upload the Jar (Runtime: Java) -> Set Handler as aws.lambda.helloWorld -> Set other params as required -> Save -> Test (Pass any Json & it will return the Json along with metadata as output)
```

### Lambda Setup - Database Password

```
Database Password needs to be passed as an encrypted environment variable. Below are the steps:

Open AWS Lambda GUI -> Code -> Environment Variables
db_password - "password here"

```


### Test Execution

![Sample](doc/screenshot.png)


### Upgrade

```
aws lambda update-function-code --zip-file fileb://./target/aws-lambda-hello-world-0.1.0-standalone.jar --function-name clojure-hello-world --profile GR_GG_COF_AWS_Sandbox_Dev_Developer
```


### Delete

```
aws lambda delete-function  --function-name clojure-hello-world --profile GR_GG_COF_AWS_Sandbox_Dev_Developer
```
