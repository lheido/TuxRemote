TuxRemote
=========

Android app to control your linux desktop via ssh (with Jsch).

##Dependencies
```
# wmctrl, openssh-server
```

##TuxRemote.config

```config
[APP NAME:wmctrl_name]
cmd_name:cmd_sh:icon_path
```

##ToDo
 - Revoir l'histoire avec la liste des commandes
 - NavigationDrawer:
    - itemLongClick -> menu (kill all without it/etc...)
    - swipeToDismiss
 - Settings
    - save server's password into database (check).
    - about TuxRemote
    - Connexion auto with default server (maybe later).
 

##Commands
 - Shutdown:
```
dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager  org.freedesktop.ConsoleKit.Manager.Stop
```
 - Restart
```
dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager org.freedesktop.ConsoleKit.Manager.Restart
```
