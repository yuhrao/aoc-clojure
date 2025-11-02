(defproject aoc-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.2"]
                 [buddy/buddy-core "1.11.423"]
                 [metosin/malli "0.19.2"]
                 ;; TODO: Move this to an specific profile
                 [nubank/matcher-combinators "3.9.2"]]
  :main ^:skip-aot aoc-clojure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
