#!/bin/sh

echo "Installation des paquets wmctrl et openssh-server"
sudo apt-get install wmctrl openssh-server

echo "Création du fichier de config : ~/.config/TuxRemote/tuxRemote.config"
cd ~/.config
mkdir -p TuxRemote
cd TuxRemote
if [ -f tuxRemote.config ]
 then
    echo "Le fichier tuxRemote.config existe déjà!"
 else
    echo "création du fichier tuxRemote.config"
    echo "plop" > tuxRemote.config
fi