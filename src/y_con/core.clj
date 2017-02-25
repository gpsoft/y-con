(ns y-con.core)

;;Lambda
(fn [x] (+ x 1))  ;;closed (combinator)
(fn [x] (+ x y))  ;;open

;;Function application
((fn [x] (+ x 1)) 3)
((fn [x] (+ x y)) 3)
(let [y 5]
  ((fn [x] (+ x y)) 3))

;;Use lambda for naming things
((fn [add1]
   (add1 3))
 (fn [x] (+ x 1)))

((fn [mk-add-y]
   ((mk-add-y 5) 3))
 (fn [y]
   (fn [x] (+ x y))))

;;Recursion with def
(def len
  (fn [lst]
    (if (empty? lst)
      0
      (+ 1 (len (rest lst))))))
(len [1 2 3])

;;Without def
((fn [mk-len]
   ((mk-len mk-len) [1 2 3]))
 (fn [mk-len']
   (fn [lst]
     (if (empty? lst)
       0
       (+ 1 ((mk-len' mk-len')
             (rest lst)))))))

(fn [len]
  (fn [lst]
    (if (empty? lst)
      0
      (+ 1 (len (rest lst)))))) ;; How to use this?

;;Y-combinator and variants
(def y
  (fn [f] ((fn [g] (f (g g)))
           (fn [h] (f (h h))))))
(def z
  (fn [f] ((fn [g] (fn [x] ((f (g g)) x)))
           (fn [h] (fn [x] ((f (h h)) x))))))
(def yz
  (fn [f] ((fn [g] (f (g g)))
           (fn [h] (fn [x] ((f (h h)) x))))))
(def zy
  (fn [f] ((fn [g] (fn [x] ((f (g g)) x)))
           (fn [h] (f (h h))))))
(def y'
  (fn [f] ((fn [g] (g g))
           (fn [h] (f (h h))))))
(def z'
  (fn [f] ((fn [g] (g g))
           (fn [h] (fn [x] ((f (h h)) x))))))

;;In action
(def mk-len
  (fn [len]
    (fn [lst]
      (if (empty? lst)
        0
        (+ 1 (len (rest lst)))))))
((y  mk-len) [1 2 3]) ;;Stack Overflow
((z  mk-len) [1 2 3])
((yz mk-len) [1 2 3])
((zy mk-len) [1 2 3]) ;;SO
((y' mk-len) [1 2 3]) ;;SO
((z' mk-len) [1 2 3])

(def mk-fact
  (fn [fact]
    (fn [n]
      (if (= n 1) 1 (* n (fact (dec n)))))))
((y  mk-fact) 10)   ;;SO
((z  mk-fact) 10)
((yz mk-fact) 10)
((zy mk-fact) 10)   ;;SO
((y' mk-fact) 10)   ;;SO
((z' mk-fact) 10)

