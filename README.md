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
    - Fonction qui renvoit la liste des commandes liées à une app.
    - Fonction qui définit la liste des icones à télécharger, avec leur nom sur le serveur et leur nom local.
    - A la connection supprimer les icones inutiles et télécharger les icones manquant indiqués dans le fichier de conf.
 - NavigationDrawer:
    - itemLongClick -> menu (kill all without it/etc...) 
 - Settings
    - save server's password into database (check).
    - about TuxRemote
    - Connexion auto with default server (maybe later).
    - UndoHideDelay for swipe to dismiss appList


##Commands
 - Shutdown:
```
dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager  org.freedesktop.ConsoleKit.Manager.Stop
```
 - Restart
```
dbus-send --system --print-reply  --dest=org.freedesktop.ConsoleKit /org/freedesktop/ConsoleKit/Manager org.freedesktop.ConsoleKit.Manager.Restart
```

 - Wmctrl:
```
wmctrl -lpx | awk  'BEGIN{FS=" "} {printf $1" " $3" "$4" " ;for(i=6; i<=NF; i++){printf $i" "}; printf "\n"} '
```
