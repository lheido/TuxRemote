package com.tuxremote.app.TuxeRemoteSsh;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.tuxremote.app.TuxRemoteUtils;


public class SshSession {
	private JSch jsch;
	private Session session;
	private UserInfo ui;


	//Constructeur
	public SshSession(String userName, String hostName, String sshKeyRoot, int port) {
		init(userName, hostName, sshKeyRoot, port);
	}

	//réinit de la session

	public void init(String userName, String hostName, String sshKeyRoot, int port){
		jsch=new JSch();

		try {
			if(sshKeyRoot!=null && !sshKeyRoot.equals("")){
				jsch.addIdentity(new File(sshKeyRoot).getAbsolutePath());
			}

			session = jsch.getSession(userName, hostName , port);
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//			session=jsch.getSession(user, host, 22);
		ui=new MyUserInfo();
		session.setUserInfo(ui);
	}

	//connecter la session
	public boolean connect(){
		try {
			session.connect();
		} catch (JSchException e) {
            e.printStackTrace();
            return false;
		}
		return true;
	}

	//connecter la session
	public void disconnect(){
		session.disconnect();
	}

	public Channel openChannel(String channel) throws JSchException {

		return session.openChannel(channel);
	}

    public void scpImage(Context context, String fileName){
        FileOutputStream fos=null;
        // exec 'scp -f rfile' remotely
        String command="cat "+ TuxRemoteUtils.ICONS_PATH+fileName;
        Channel channel;
        try {
            channel = openChannel("exec");
            ((ChannelExec)channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out=channel.getOutputStream();
            InputStream in=channel.getInputStream();

            channel.connect();

            byte[] buf=new byte[1024];

            // send '\0'
            buf[0]=0; out.write(buf, 0, 1); out.flush();
            int c='C';
            while(c=='C'){
                c=checkAck(in);
                if(c=='C'){
                    //				break;
                    //			}

                    // read '0644 '
                    in.read(buf, 0, 5);

                    long filesize=0L;
                    while(true){
                        if(in.read(buf, 0, 1)<0){
                            // error
                            break;
                        }
                        if(buf[0]==' ')break;
                        filesize=filesize*10L+(long)(buf[0]-'0');
                    }

                    String file=null;
                    for(int i=0;;i++){
                        in.read(buf, i, 1);
                        if(buf[i]==(byte)0x0a){
                            file=new String(buf, 0, i);
                            break;
                        }
                    }

                    //System.out.println("filesize="+filesize+", file="+file);

                    // send '\0'
                    buf[0]=0; out.write(buf, 0, 1); out.flush();

                    // Open fileOutput with fileName and write byteArray
                    FileOutputStream flop = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                    if(flop != null) {
                        flop.write(file.getBytes());
                        flop.close();
                    }

                    // read a content of lfile
//                    fos=new FileOutputStream(prefix==null ? lfile : prefix+file);
//                    int foo;
//                    while(true){
//                        if(buf.length<filesize) foo=buf.length;
//                        else foo=(int)filesize;
//                        foo=in.read(buf, 0, foo);
//                        if(foo<0){
//                            // error
//                            break;
//                        }
//                        fos.write(buf, 0, foo);
//                        filesize-=foo;
//                        if(filesize==0L) break;
//                    }
//                    fos.close();
//                    fos=null;
//
//                    if(checkAck(in)!=0){
//                        System.exit(0);
//                    }

                    // send '\0'
                    buf[0]=0; out.write(buf, 0, 1); out.flush();
                }
            }
            channel.disconnect();

        } catch (JSchException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void scp(String rfile, String lfile){
		FileOutputStream fos=null;
		String prefix = null ;
		if(new File(lfile).isDirectory()){
			prefix=lfile+File.separator;
		}
		// exec 'scp -f rfile' remotely
		String command="scp -f "+rfile;
		Channel channel;
		try {
			channel = openChannel("exec");
			((ChannelExec)channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out=channel.getOutputStream();
			InputStream in=channel.getInputStream();

			channel.connect();

			byte[] buf=new byte[1024];

			// send '\0'
			buf[0]=0; out.write(buf, 0, 1); out.flush();
			int c='C';
			while(c=='C'){
				c=checkAck(in);
				if(c=='C'){
					//				break;
					//			}

					// read '0644 '
					in.read(buf, 0, 5);

					long filesize=0L;
					while(true){
						if(in.read(buf, 0, 1)<0){
							// error
							break; 
						}
						if(buf[0]==' ')break;
						filesize=filesize*10L+(long)(buf[0]-'0');
					}

					String file=null;
					for(int i=0;;i++){
						in.read(buf, i, 1);
						if(buf[i]==(byte)0x0a){
							file=new String(buf, 0, i);
							break;
						}
					}

					//System.out.println("filesize="+filesize+", file="+file);

					// send '\0'
					buf[0]=0; out.write(buf, 0, 1); out.flush();

					// read a content of lfile
					fos=new FileOutputStream(prefix==null ? lfile : prefix+file);
					int foo;
					while(true){
						if(buf.length<filesize) foo=buf.length;
						else foo=(int)filesize;
						foo=in.read(buf, 0, foo);
						if(foo<0){
							// error 
							break;
						}
						fos.write(buf, 0, foo);
						filesize-=foo;
						if(filesize==0L) break;
					}
					fos.close();
					fos=null;

					if(checkAck(in)!=0){
						System.exit(0);
					}

					// send '\0'
					buf[0]=0; out.write(buf, 0, 1); out.flush();
				}
			}
			channel.disconnect();

		} catch (JSchException e){
            e.printStackTrace();
        }
        catch (IOException e) {
			e.printStackTrace();
		}

	}

    public void setCommandNoReturn(String commande){
        Channel channel;
        try {
            channel = session.openChannel("exec");
            if(channel == null) return;
            ((ChannelExec)channel).setCommand(commande);
            channel.setInputStream(null);

            channel.connect();
            channel.disconnect();

        } catch (JSchException e){
            e.printStackTrace();
        }
    }

	public BashReturn SetCommand(String commande){
		Channel channel;
		String result=new String("");
		String erreur=new String("");
		try {
			channel = session.openChannel("exec");
            if(channel == null) return null;
			((ChannelExec)channel).setCommand(commande);
			channel.setInputStream(null);

			((ChannelExec)channel).setErrStream(System.err);
			InputStream in=channel.getInputStream();
			InputStream err= ((ChannelExec)channel).getErrStream();

			channel.connect();

			byte[] tmp=new byte[1024];
			while(true){
				while(in.available()>0){
					int i=in.read(tmp, 0, 1024);
					if(i>=0){
						result=result.concat(new String(tmp, 0, i));
					}
				}
				while(err.available()>0){
					int i=err.read(tmp,0,1024);
					if(i>=0){
						erreur=erreur.concat(new String(tmp,0,i));
					}
				}
				if(channel.isClosed()){
					if(in.available()>0) continue;
					//					System.out.println("exit-status: "+channel.getExitStatus());
					break;
				}
				try{Thread.sleep(1000);}catch(Exception ee){}
			}
			channel.disconnect();

		} catch (JSchException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

		return new BashReturn(result, erreur);
	}
	static int checkAck(InputStream in) throws IOException{
		int b=in.read();
		// b may be 0 for success,
		//          1 for error,
		//          2 for fatal error,
		//          -1
		if(b==0) return b;
		if(b==-1) return b;

		if(b==1 || b==2){
			StringBuffer sb=new StringBuffer();
			int c;
			do {
				c=in.read();
				sb.append((char)c);
			}
			while(c!='\n');
			
			if(b==1){ // error
				System.out.print(sb.toString());
			}
			if(b==2){ // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}

    public void setPassword(String pass){
        session.setPassword(pass);
    }

}
