<h1 align="center">
	<img
		alt="wirez"
		src="https://raw.githubusercontent.com/DougCodez/WireZ/master/img/img.png">
</h1>

<h3 align="center">
  WireZ is a Minecraft platform diagnostics tool used to collect system statistics and interactive database information
</h3>

## What's WireZ job?
WireZ has two jobs as a whole:

* **System Diagnostics Tool**: Gather system statistics of your platform
* **Interactive Database Handler**: Multi database interactive system to handle databases

## System Diagnostics Tool

WireZ system diagnostics tool allows you to gather system statistics of your platform. WireZ system diagnostics tool can be broken up in these components:

* **CPU Profiler**: View performance aspects of a platform.
* **Memory Summarizer**: View memory aspects of a platform.
* **Thread Information**: View the use of threads of a platform.
* **In Game Health Format Graphs**: View the health of a certain system component.
* **Websocket Graphs**: View the % of system statistics that are transferred through websockets.  

## :electric_plug: CPU Profiler:

WireZ CPU profiler is used to collect the use of system performance.

**Benefits**:

* **Lightweight** - Will have zero impact on your platform's performance
* **Collection** - Collects real time and real time averages of both system and processed CPU
* **Format** - Show real time percentages with a readable chat graph

## 🔌 Memory Summarizer:

WireZ Memory summarizer gives you a clue of how your platform's memory is running.

* **Disk Space** - Shows used disk space out of the total disk space a platform has. 
* **RAM Usage** - Shows used RAM out of the total RAM a platform has.  
* **Heap Summary** - Full analysis of your platform's memory. JVM interaction to see the amount of bytes and instances each Java class takes up.
* **Heap Summary Format** - Given information of a Heap Summary as an output comes back as a https request paste with a key. 

## 🔌 Thread Information: 

WireZ Thread Information aspect allows you to inspect a platforms use of threads, and to perform thread dumps.

* **Information Given** - Using ThreadMXBean the information give is the threads running on a platform and extra details (id, name, state, etc)
* **Thread Dumps** - Gives off stack traces of the plaforms use of threads.
* **Format** - Given information of Thread Information and Thread Dumps as an output comes back as a https request paste with a key. 

## 🔌 In Game Health Graphs:

Chat formatted graphs with a use of 0-100 with specfic color codes.

* **Support** - These graphs support CPU use, Disk Space, and RAM usage. 

## 🔌 Websocket Graphs:

Live system statistics that are transferred between a maintained websocket server, and are received from a websocket client (html file)

* **Lighweight** - The use of websockets are fast and reliable as data transfer is happening through sockets.
* **Use** - It is simple to use as you only have to run an html file to view the graph live of every system component. 
* **HTML Usage** - The HTML file can be found under the static directory if you want to use this for your platform. 

## Database Handler:

WireZ interactive database system supports players to connect to more than one database at a time in game. WireZ database handler can be broken up in these components:

* **Multi Platform** - the use of multiple datasources a user can interact with.
* **Table Interaction** - table interactions a user can make. 

## 🔌 Multi Platform:

WireZ allows for users to connect to more than one datasource, and interact with its database in game.

* **Connection Inits** - You as a user can connect to multiple datasources. Attempts to connect to an already connected datasource will be blocked.
* **Disconnection** - Disconnection of a datasource is closed properly. If disconnected, any user is free to connect any time.
* **Viewing** - Can enlist the use of datasources you're connected to, or can enlist other users datasource connections.


## 🔌 Table Interaction

WireZ database handler most importantly allows you to inspect tables at every anagle.

* **Table Dump** Dumps a copy or exact replica of a table into a CSV file of the database you're currently connected to.
* **Table List** Get a list of tables a database contains. Output comes back as a https request paste with a key.

## Download

The link to download WireZ can be found [Here](https://www.spigotmc.org/resources/⚡-wirez-a-multi-platform-diagnostics-tool-lightweight-optimizable-⚡.100188/). It supports spigot, bungeecord, velocity, and velocity4
