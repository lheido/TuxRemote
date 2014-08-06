TuxRemote
=========

Android app to control your linux desktop via ssh (with Jsch).

##Dependencies
```
# wmctrl, openssh-server
```

##config.xml

```xml
<Application
    name=""
    wmctrl_name=""
    icon="">
    <Command
        name=""
        cmd=""
        icon=""/>
</Application>
```


##ToDo
 - liste des app dans le config.xml:
    - Fonction qui définit la liste des icones à télécharger, avec leur nom sur le serveur et leur nom local.
    - A la connection supprimer les icones inutiles et télécharger les icones manquant indiqués dans le fichier de conf.
 - Filtrer les app wmctrl avec les app du config.xml
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
wmctrl -lpx | awk  'BEGIN{FS=" "} NF>4 && $4!="N/A"{printf $1" " $3" "$4" " ;for(i=6; i<=NF; i++){printf $i" "}; printf "\n"} '
```
