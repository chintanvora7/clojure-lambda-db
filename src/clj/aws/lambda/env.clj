(ns aws.lambda.env)

(defn fn-get-db-from-env [db-key]
  "Dependency: db-key as db_password, Lambda env variable
   Description: Fn takes db_password, returns the value set in Runtime Env with same key
                else returns default as empty string
   Note: Please send password in empty string below in dev mode. Do not publish pwd to git!"
  (let [value (System/getenv db-key)]
    (if (nil? value)
      ""
      value)))

(defn fn-get-pool-from-env [pool-key]
  "Dependency: Pool-Key as min_pool_size or max_pool_size, Lambda env variable
   Description: Fn takes min_pool_size or max_pool_size, returns the value set in Runtime Env with same key
                else returns default min_pool_size -> 3, max_pool_size -> 5"
  (let [value (System/getenv pool-key)]
    (case pool-key
      "min_pool_size" (if (nil? value)
                        "3"
                        value)
      "max_pool_size" (if (nil? value)
                        "5"
                        value))))

(defn fn-get-value-from-env [key]
  "Dependency: Environment Variable set in Lambda UI -> key should be from db_password, min_pool_size, max_pool_size
   Description: Given a Lambda environment with env variable eg. db-password, fn returns password
                else returns empty string. For Keys involving Numbers eg. pool-size, fn returns java.lang.Long
                else returns defaults 3 -> min_pool_size & 5 -> max_pool_size
   Note: Please send password in empty string below in dev mode. Do not publish pwd to git!"
  (case key
    "db_password" (fn-get-db-from-env key)
    "min_pool_size" (fn-get-pool-from-env key)
    "max_pool_size" (fn-get-pool-from-env key)
    nil))