# Graphical Timetable Generator
A program to create a graphical timetable from a formatted file.

#### Contents
1. Installation and Use
2. Text file requirements
3. Need help?

### Installation and Use
Download the latest `.jar` file from the [Releases page](https://github.com/Daniel-Gill/GraphicalTimetableGenerator/releases).

This program is designed to be run from the command line. Assuming you have Java installed, you'll need a command like this:

`java -jar <file path to GraphicalTimetableGenerator-1.0.jar> -i=<file path to .txt file>`

For example:

`java -jar C:\Users\test_user\Desktop\GraphicalTimetableGenerator-1.0.jar -i=C:\Users\test_user\Desktop\test.txt`

Once the window has opened, there are several useful features:
* Holding right click on the graph and dragging will zoom in on the selected area.
* On the right click menu you can:
  * Save your graph as a `.png`.
  * Zoom in and out.
  * Change the appearance of the graph.

### Text file requirements
The text file is required to be in the below format:

```
STATIONS
<number of stations>
<list
of
all
stations,
one
on 
each
line>
;
SERVICES
<number of services>
<service 1 ID>
<list of stops
with arrival and
departure times
denoted by a or d>
;
<service 2 ID>
<list of stops
with arrival and
departure times
denoted by a or d>
;
```

Here's a small example:
```
STATIONS
3
Alpha
Bravo
Charlie
;
SERVICES
2
1A00
Alpha d12:00
Charlie a12:10
;
2B00
Alpha d12:03
Bravo a12:05 d12:06
Charlie a12:13
;
```

An example longer file can be found in the repository [here](https://github.com/Daniel-Gill/GraphicalTimetableGenerator/blob/master/src/test/java/test.txt).

### Need help?

Feel free to open an issue [here](https://github.com/Daniel-Gill/GraphicalTimetableGenerator/issues).
