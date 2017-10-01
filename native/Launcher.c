/**
 * Launcher.c
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <mach-o/dyld.h>
#include <CoreServices/CoreServices.h>

#define ORACLE_JAVA_LAUNCHER  "JavaAppLauncher"
#define APPLE_JAVA_LAUNCHER   "JavaApplicationStub"

char * getJavaLauncherExecutablePath(char *javaLauncherExecutable) {
  // Retrieve executable path
  uint32_t size = sizeof(char) * 1024;
  char *executablePath = malloc(size);
  if (_NSGetExecutablePath(executablePath, &size) != 0) {
    free(executablePath);
    executablePath = malloc(size);
    _NSGetExecutablePath(executablePath, &size);
  }

  // Replace last path element by javaExecutable
  char *javaLauncherExecutablePath = malloc(sizeof(char) *
          (strlen(executablePath) + strlen(javaLauncherExecutable)));
  strcpy(javaLauncherExecutablePath, executablePath);
  strcpy(strrchr(javaLauncherExecutablePath, '/') + 1,
         javaLauncherExecutable);

  free(executablePath);
  return javaLauncherExecutablePath;
}

int main(int argc, char *argv[])
{
  // Retrieve Mac OS X version
  SInt32 majorVersion,minorVersion;
  Gestalt(gestaltSystemVersionMajor, &majorVersion);
  Gestalt(gestaltSystemVersionMinor, &minorVersion);

  char *executablePath;
  if (majorVersion >= 10 && minorVersion >= 7) {
    executablePath = getJavaLauncherExecutablePath(ORACLE_JAVA_LAUNCHER);
  } else {
    executablePath = getJavaLauncherExecutablePath(APPLE_JAVA_LAUNCHER);
  }
  int returnedValue = execv(executablePath, argv);
  free(executablePath);

  return returnedValue;
}