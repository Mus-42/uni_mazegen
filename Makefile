RAYLIB?=dep/jaylib-5.0.0-0-arm.jar
JAVA_SOURCES:=src/Main.java src/Maze.java src/Random.java\
	src/Maze2D.java src/Maze3D.java src/MazeGenerator.java\
	src/TileDisplay.java src/AppState.java\
	src/MazeDisplay2D.java src/MazeDisplay3D.java
RESOURCES:=res/tiles32.png

.PHONY: run
run: build/maze.jar 
	java -jar build/maze.jar

.PHONY: clean
clean:
	rm -r build/class/*
	rm -r doc/*
	rm build/maze.jar

.PHONY: create_build_dirs
create_build_dirs:
	mkdir build build/class doc

.PHONY: doc
doc: ${JAVA_SOURCES} 
	javadoc -cp ${RAYLIB} -d doc ${JAVA_SOURCES}

build/maze.jar: ${JAVA_SOURCES} ${RESOURCES} 
	cp ${RAYLIB} build/maze.jar
	javac -cp build/maze.jar -d build/class ${JAVA_SOURCES}
    # add class files
	jar ufe build/maze.jar knu.mus.maze.Main -C build/class .
    # add resources
	jar ufe build/maze.jar knu.mus.maze.Main ${RESOURCES}

