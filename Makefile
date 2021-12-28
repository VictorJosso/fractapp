ifeq ($(OS),Windows_NT)     # is Windows_NT on XP, 2000, 7, Vista, 10...
    WRAPPER := gradlew.bat
    LAUNCH_FILE := fractapp.bat
    CHMOD_COMMAND :=
else
    WRAPPER := ./gradlew  # same as "uname -s"
    LAUNCH_FILE := fractapp
    CHMOD_COMMAND := chmod +x fractapp
endif

build:
	$(WRAPPER) shadowJar
	cp config/$(LAUNCH_FILE) .
	$(CHMOD_COMMAND)

clean:
	rm -fr build/ $(LAUNCH_FILE)
