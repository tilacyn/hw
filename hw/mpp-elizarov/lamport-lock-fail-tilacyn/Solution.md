## 1. Некорректное исполнение
[![Build Status](https://travis-ci.com/ITMO-MPP-2017/lamport-lock-fail-<your_GitHub_account>.svg?token=B2yLGFz6qwxKVjbLm9Ak&branch=master)](https://travis-ci.com/ITMO-MPP-2017/lamport-lock-fail-<your_GitHub_account>)

Пусть у нас есть два потока, t0 и t1. Проблема состоит в том в том, что после того как t0 придет в секцию choosing запомнит для себя переменную my = 1 (3), он станет самым сладшим потоком, так как будет соответствовать паре (1, 0), но так как он только для себя запомнил это значение, t1 вообще не в курсе, что t0 находится в секции choosing, и t1 может пройти в КС, а затем туда же попадет и t0, так как у него самый младший номер в очереди из всех возможных.


## 2. Исправление алгоритма

Можно было бы не заводя массив choosing завести volatile int choosingN, один на всех. Перед входом в секцию choosing поток будет делать choosingN++, после выхода choosingN--.

(exists k : choosing[k]) <=> (choosingN > 0)
