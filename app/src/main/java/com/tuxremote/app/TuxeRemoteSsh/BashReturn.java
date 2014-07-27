package com.tuxremote.app.TuxeRemoteSsh;
import java.util.ArrayList;


public class BashReturn {
	private ArrayList<String> _retour;
	private ArrayList<String> _erreur;
	public BashReturn(String retour, String erreur) {
		_retour=new ArrayList<String>();
		_erreur=new ArrayList<String>();
		String []tmp;
		tmp=retour.split("\n");
		for(int i=0;i<tmp.length;i++){
			_retour.add(tmp[i]);
		}

		tmp=erreur.split("\n");
		for(int i=0;i<tmp.length;i++){
			_erreur.add(tmp[i]);
		}
	}

	public ArrayList<String> getBashReturn(){
		return _retour;
	}
	
	public ArrayList<String> getBashError(){
		return _erreur;
	}

}
