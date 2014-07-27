package com.tuxremote.app.TuxeRemoteSsh;

import android.widget.Toast;

import com.jcraft.jsch.UserInfo;
import com.tuxremote.app.Global;

public class MyUserInfo implements UserInfo{
		public String getPassword(){ return passwd; }
		public boolean promptYesNo(String str){
			return true;
		}
		String passwd;

		public String getPassphrase(){ return null; }
		public boolean promptPassphrase(String message){ return true; }
		public boolean promptPassword(String message){
//			Object[] ob={passwordField};
//			int result=
//					JOptionPane.showConfirmDialog(null, ob, message,
//							JOptionPane.OK_CANCEL_OPTION);
//			if(result==JOptionPane.OK_OPTION){
//				passwd=passwordField.getText();
//				return true;
//			}
//			else{
//				return false;
//			}
            return true;
		}
		public void showMessage(String message){
            Toast.makeText(Global.getStaticContext(), message, Toast.LENGTH_LONG).show();
		}

	}
