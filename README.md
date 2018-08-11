# servator

An experiment in 'no-cost' observer patterns. I'd like an n-pass macro expansion mechanic, but since I don't have that, I'll start with an attempt to exploit my least favorite aspect of multimethods -- behavior can changes based on some code existing somewhere and you have no idea where!

## Usage

```clojure
(signal :some-scope/some-signal
  (reduce +
          (do-something)))
		  
(observe :some-scope/some-signal
         (fn [payload]
		     (do-something-with-payload payload)))
```

[Examples](test/servator/core_test.clj).

## License

Copyright © 2018 Timothy Dean

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
