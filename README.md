TuxRemote
=========

Android app to control your linux desktop via ssh (with Jsch).

##Dependencies
```
# wmctrl, openssh-server
```

##TuxRemote.config

```config
[APP NAME]
cmd_name:cmd_sh:icon_path
```

##Commands
```
dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager  org.freedesktop.ConsoleKit.Manager.Stop
```
```
dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager org.freedesktop.ConsoleKit.Manager.Restart
```
