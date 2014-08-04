#!/bin/sh

echo "Installation des paquets wmctrl et openssh-server"
sudo apt-get install wmctrl openssh-server

echo "Création du fichier de config : ~/.config/TuxRemote/tuxRemote.config"
cd ~/.config
mkdir -p TuxRemote
cd TuxRemote
config=config.xml
if [ -f $config ]
 then
    echo "Le fichier $config existe déjà!"
 else
    echo "création du fichier $config"
    echo "plop" > $config
fi