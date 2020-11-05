|                 	| 256mb                                 	| 4gb                                   	|
|-----------------	|---------------------------------------	|---------------------------------------	|
| SerialGC        	| Young counts: 7, avg duration: 37 ms  	| Young counts: 1, avg duration: 336 ms 	|
|                 	| Old counts: 40, avg duration: 232 ms  	| приложение отработало без OOM         	|
|                 	| Work time [200] sec                   	|                                       	|
|                 	|                                       	|                                       	|
| ConcMarkSweepGC 	| Young counts: 20, avg duration: 11 ms 	| Young counts: 7, avg duration: 180 ms 	|
|                 	| Old counts: 84, avg duration: 492 ms  	| Work time [1121] sec                  	|
|                 	| Work time [231] sec                   	| приложение отработало без OOM         	|
|                 	|                                       	|                                       	|
| G1GC            	| Young counts: 42, avg duration: 16 ms 	| Young counts: 6, avg duration: 56 ms  	|
|                 	| Old counts: 12, avg duration: 229 ms  	| приложение отработало без OOM         	|
|                 	| Work time [225] sec                   	|                                       	|

adopt-openjdk-13.0.2 
