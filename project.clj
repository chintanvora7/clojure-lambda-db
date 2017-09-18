(defproject aws-lambda-helloWorld "0.1.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 ;; AWS
                 [com.amazonaws/aws-lambda-java-core "1.1.0"] ;; core
                 ;;[com.amazonaws/aws-java-sdk-lambda "1.10.76"] ;; λ


                 ;;DB Pooling & Prepared Statment Libraries

                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.1"]
                 [honeysql "0.6.3"]
                 [org.clojure/java.jdbc "0.5.0"]
                 [org.postgresql/postgresql "9.4.1208.jre7"]
                 ]
  :java-source-paths ["src/java"]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :aot :all)
