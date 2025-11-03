(ns server.core
  (:require
   [io.pedestal.connector :as conn]
   [io.pedestal.http.http-kit :as hk]
   [io.pedestal.interceptor :as interceptor]
   [clojure.data.json :as json]
   [aoc-clojure.registry.core :as registry]
   ;; Load all 2015 AOC days
   [aoc-clojure.2015.day-1]
   [aoc-clojure.2015.day-2]
   [aoc-clojure.2015.day-3]
   [aoc-clojure.2015.day-4]
   [aoc-clojure.2015.day-5]
   [aoc-clojure.2015.day-6]
   [aoc-clojure.2015.day-7]
   [aoc-clojure.2015.day-8]
   [aoc-clojure.2015.day-9])
  (:import
   [java.io InputStreamReader]))

(def json-request-parser
  "Interceptor to parse JSON request bodies for PUT/POST/DELETE/PATCH methods"
  (interceptor/interceptor
   {:name ::json-request-parser
    :enter (fn [context]
             (let [request (:request context)
                   method (:request-method request)
                   body (:body request)]
               (if (and (some #{method} #{:put :post :delete :patch})
                        (some? body))
                 (try
                   (let [parsed-body (json/read (InputStreamReader. body))]
                     (assoc-in context [:request :json-params] parsed-body))
                   (catch Exception e
                     (assoc context :response
                            {:status 400
                             :body {:error "Invalid JSON in request body"
                                    :details (ex-message e)}})))
                 context)))}))

(def json-response-encoder
  "Interceptor to encode response bodies as JSON when body is not nil"
  (interceptor/interceptor
   {:name ::json-response-encoder
    :leave (fn [context]
             (let [response (:response context)
                   body (:body response)]
               (if (and (some? body)
                        (not (string? body)))
                 (assoc-in context [:response :body] (json/write-str body))
                 context)))}))

(defn hello-world [_req]
  {:status 200
   :body "Hello World!"})

(defn execute-aoc-day [req]
  #p req
  (let [{day-str :day year-str :year} (:path-params req)]
    (if (or (empty? day-str) (empty? year-str))
      {:status 400
       :body {:error "you must provide a valid year an day"}}
      (try
        (let [year (Integer/parseUnsignedInt year-str)
              day (Integer/parseUnsignedInt day-str)]
          {:status 200
           :body (registry/execute-day {:year year
                                        :day day})})
        (catch clojure.lang.ExceptionInfo ex
          (let [{:keys [cause]} (ex-data ex)
                message (ex-message ex)]
            {:status (if (= cause :not-found)
                       404
                       500)
             :body {:error message}}))
        (catch Throwable ex
          {:status 500
           :body {:error "unexpected error"
                  :cause (ex-message ex)}})))))

(defn routes []
  #{["/api/hello" :get hello-world :route-name :hello-world]
    ["/api/aoc/:year/:day" :post execute-aoc-day :route-name :aoc]})

(defn connector []
  (-> (conn/default-connector-map 8080)
      conn/with-default-interceptors
      (conn/with-routes (routes))
      (conn/with-interceptors [json-request-parser json-response-encoder])
      (hk/create-connector nil)))

(defonce server (atom nil))

(defn start! []
  (when (nil? @server)
    (reset! server (conn/start! (connector)))))

(defn stop! []
  (swap! server conn/stop!)
  (reset! server nil))

(comment

  (start!)

  (stop!))
