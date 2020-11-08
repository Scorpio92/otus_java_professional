|                 	| 256mb                                 	| 1gb                                         	|
|-----------------	|---------------------------------------	|---------------------------------------------	|
| SerialGC        	| Young counts: 7, avg duration: 37 ms  	| Young counts: 5/5, max duration: 132/140 ms 	|
|                 	| Old counts: 40, avg duration: 232 ms  	| iteration: 761/764                          	|
|                 	| Work time [200] sec                   	| приложение отработало без OOM               	|
|                 	|                                       	|                                             	|
| ConcMarkSweepGC 	| Young counts: 20, avg duration: 11 ms 	| Young counts: 5, max duration: 186/192 ms   	|
|                 	| Old counts: 84, avg duration: 492 ms  	| Old counts: 2, max duration: 5881/5730 ms   	|
|                 	| Work time [231] sec                   	| iteration: 767/764                          	|
|                 	|                                       	| приложение отработало без OOM               	|
|                 	|                                       	|                                             	|
| G1GC            	| Young counts: 42, avg duration: 16 ms 	| Young counts: 12, max duration: 43/43 ms    	|
|                 	| Old counts: 12, avg duration: 229 ms  	| iteration: 491/490                          	|
|                 	| Work time [225] sec                   	| приложение отработало без OOM               	|

adopt-openjdk-13.0.2 
