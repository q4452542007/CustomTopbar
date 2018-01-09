package mapsoft.com.costomtopbar.service.socket;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mapsoft.com.costomtopbar.IBackService;
import mapsoft.com.costomtopbar.activity.MainActivity;
import mapsoft.com.costomtopbar.constant.Constant;
import mapsoft.com.costomtopbar.db.ChatSQLiteHelper;
import mapsoft.com.costomtopbar.db.SharedPreferenceManager;
import mapsoft.com.costomtopbar.jt808.HandleMessage;
import mapsoft.com.costomtopbar.jt808.MsgDecoder;
import mapsoft.com.costomtopbar.jt808.MsgEncoder;
import mapsoft.com.costomtopbar.jt808.PackageData;

import mapsoft.com.costomtopbar.jt808.ServerTextMsg;
import mapsoft.com.costomtopbar.jt808.TPMSConsts;
import mapsoft.com.costomtopbar.util.GbkToChinese;
import mapsoft.com.costomtopbar.util.Utils;

public class BackService extends Service {
	private static final String TAG = "BackService";

	private static final byte[] sucess = {0x00};
	/** 心跳检测时间  */
	private static final long HEART_BEAT_RATE = 3 * 1000;

	private static final long RIGSTER_BEAT_RATE = 5 * 1000;
	/** 主机IP地址  */
	private static String HOST = "";
	/** 端口号  */
	public static int PORT = 0;
	/** 消息广播  */
	public static final String MESSAGE_ACTION = "org.feng.message_ACTION";

	/** 心跳广播  */
	public static final String HEART_BEAT_ACTION = "org.feng.heart_beat_ACTION";

	/**注册广播 */
	public static final String RIGSTER_BEAT_ACTION = "org.feng.rigster_ACTION";

	/**鉴权广播 */
	public static final String CHECK_BEAT_ACTION = "org.feng.check_ACTION";

	/**位置广播*/
	public static final String LOCATION_UPLOAD_ACTION = "org.feng.location_upload_ACTION";

	/**文本下发广播*/
	public static final String TEXT_ISSUE_ACTION = "org.feng.text_issue_ACTION";


	public static final byte[] REGISTER = {0x7E,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x07,0x77,0x77,0x01,0x40,0x47,0x7E};

	public  static final byte[] POWER = {0x7E,0x01,0x02,0x00,0x05,0x00,0x00,0x00,0x07,0x77,0x77,0x00,0x01,0x37,0x37,0x37,0x37,0x37,0x37,0x7E};

	public static final byte[] CHECK = {0x7E,0x01,0x02,0x00,0x05,0x00,0x00,0x00,0x07,0x77,0x77,0x00,0x01,0x37,0x37,0x37,0x37,0x37,0x37,0x7E};

	public static final byte[] HEART = {0x7E, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00,0x07, 0x77, 0x77, 0x00, 0x07, 0x02, 0x7E};

	public static final String REPONSE = "7E8001000500000007777700070007000200817E";

	private static byte[] message;
	public static  String powerCode = null;

	private long sendTime = 0L;

	/** 弱引用 在引用对象的同时允许对垃圾对象进行回收  */
	private WeakReference<Socket> mSocket;

	private ReadThread mReadThread = null;

	private MsgDecoder mMsgDecoder;
	private MsgEncoder mMsgEncoder;
	private PackageData mPackageData;
	/*private ServerCommonRespMsgBody mServerCommonRespMsgBody;*/
	private HandleMessage mHandleMessage;

	private ServerTextMsg textMsg;

	private MainActivity.MyHandler mMyHandler;

	SQLiteOpenHelper openHelper;

	private IBackService.Stub iBackService = new IBackService.Stub() {
		@Override
		public boolean sendMessage(byte[] message) throws RemoteException {
			return sendMsg(message);
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return (IBinder) iBackService;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.mMsgDecoder = new MsgDecoder();
		this.mPackageData = new PackageData();
		textMsg = new ServerTextMsg();
		this.mMsgEncoder = new MsgEncoder();
		//this.mServerCommonRespMsgBody = new ServerCommonRespMsgBody();
		this.mHandleMessage = new HandleMessage();
		mMyHandler = new MainActivity.MyHandler(this);
		RigsterMessage rigsterMessage = new RigsterMessage("000000023232",1);
		message = rigsterMessage.send(TPMSConsts.terminalType,TPMSConsts.terminalId,TPMSConsts.manufacturerId);
		HOST = SharedPreferenceManager.getInstance().getString(SharedPreferenceManager.IP_SETTING,"60.191.59.13");
		PORT = Integer.valueOf(SharedPreferenceManager.getInstance().getString(SharedPreferenceManager.PORT_SETTING,"7770"));

		new InitSocketThread().start();
	}

	// 发送心跳包
	private Handler mHandler = new Handler();
	private Runnable heartBeatRunnable = new Runnable() {
		@Override
		public void run() {
			if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {

				boolean isSuccess = false;// 就发送一个\r\n过去, 如果发送失败，就重新初始化一个socket
				try {
					isSuccess = sendMsg(mMsgEncoder.encode4HeartBeatMsg(0));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!isSuccess) {
					mHandler.removeCallbacks(heartBeatRunnable);
					mReadThread.release();
					releaseLastSocket(mSocket);
					//new InitSocketThread().start();
					mHandler.post(rigsterRunnable);
				}else {
					mHandler.postDelayed(heartBeatRunnable,HEART_BEAT_RATE);
				}

			}
		}
	};

	private Runnable rigsterRunnable = new Runnable() {
		@Override
		public void run() {

				boolean isSuccess = sendMsg(message);
				if (!isSuccess) {
					mHandler.removeCallbacks(rigsterRunnable);
					mReadThread.release();
					releaseLastSocket(mSocket);
					new InitSocketThread().start();
			}
			if (mHandler != null) {
				mHandler.postDelayed(this, RIGSTER_BEAT_RATE);
			}
		}
	};
	private Runnable checkRunnable = new Runnable() {
		@Override
		public void run() {
			byte[] check = null;
			try {
				check = mMsgEncoder.encode4TerminalCheckPower(mPackageData,1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			boolean isSuccess = sendMsg(check);
			if (!isSuccess) {
				mHandler.removeCallbacks(checkRunnable);
				mReadThread.release();
				releaseLastSocket(mSocket);
				new InitSocketThread().start();
			}

		}
	};
	private Runnable terminalComRespRunnable = new Runnable() {
		@Override
		public void run() {
			byte[] tcr = null;
			try {
				tcr = mMsgEncoder.encode4TerminalCommonRespMsg(textMsg);

				Log.e(TAG,Utils.bytes2HexString(tcr));
			} catch (Exception e) {
				e.printStackTrace();
			}
			boolean isSuccess = sendMsg(tcr);
			if (!isSuccess) {
				mHandler.removeCallbacks(checkRunnable);
				mReadThread.release();
				releaseLastSocket(mSocket);
				new InitSocketThread().start();
			}

		}
	};


	public boolean sendMsg(byte[]  msg) {
		if (null == mSocket || null == mSocket.get()) {
			return false;
		}
		Socket soc = mSocket.get();
		try {
			if (!soc.isClosed() && !soc.isOutputShutdown()) {
				OutputStream os = soc.getOutputStream();
				/*String message = msg;*/
				os.write(msg);
				os.flush();
				sendTime = System.currentTimeMillis();// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
				Log.i(TAG, "发送成功的时间：" + sendTime);
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 初始化socket
	private void initSocket() throws UnknownHostException, IOException {
		Socket socket = new Socket(HOST, PORT);
		mSocket = new WeakReference<Socket>(socket);
		mReadThread = new ReadThread(socket);
		mReadThread.start();
		//mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);// 初始化成功后，就准备发送心跳包
		mHandler.postDelayed(rigsterRunnable, RIGSTER_BEAT_RATE);
		//mHandler.postDelayed(rigsterRunnable,RIGSTER_BEAT_RATE);
	}

	// 释放socket
	private void releaseLastSocket(WeakReference<Socket> mSocket) {
		try {
			if (null != mSocket) {
				Socket sk = mSocket.get();
				if (!sk.isClosed()) {
					sk.close();
				}
				sk = null;
				mSocket = null;
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class InitSocketThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				initSocket();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG,"没有网络连接");
				try {
					sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				mHandler.removeCallbacks(rigsterRunnable);
				new InitSocketThread().start();
			}
		}
	}

	public class ReadThread extends Thread {
		private WeakReference<Socket> mWeakSocket;
		private boolean isStart = true;

		public ReadThread(Socket socket) {
			mWeakSocket = new WeakReference<Socket>(socket);
		}

		public void release() {
			isStart = false;
			releaseLastSocket(mWeakSocket);
		}

		@SuppressLint("NewApi")
		@Override
		public void run() {
			super.run();
			Socket socket = mWeakSocket.get();
			if (null != socket) {
				InputStream is = null;
				try {
					is = socket.getInputStream();
					byte[] buffer = new byte[1024 * 4];
					int length = 0;
					while (!socket.isClosed() && !socket.isInputShutdown()
							&& isStart && ((length = is.read(buffer)) != -1)) {
						if (length > 0) {
							byte[] ss = new byte[length];
							/*String message = new String(Arrays.copyOf(buffer,
									length)).trim();*/
							System.arraycopy(buffer, 0, ss, 0, length);

							mPackageData = mMsgDecoder.bytes2PackageData(ss);
							String message = Utils.bytes2HexString(ss);
							Log.e(TAG, "收到服务器发送来的消息："+message);
							// 收到服务器过来的消息，就通过Broadcast发送出去

							int select = mHandleMessage.handleAllMessage(mPackageData);
							switch (select) {
								case 10:
									//注册成功
									Intent rigster = new Intent(RIGSTER_BEAT_ACTION);
									rigster.putExtra("result","0");
									sendBroadcast(rigster);
									//powerCode = message.substring(32,42);
									if (mHandler != null) {
										mHandler.removeCallbacks(rigsterRunnable);
										mHandler.post(checkRunnable);
									}
									break;
								case 11:
									//注册失败,具体处理后面更改
									Intent register1 = new Intent(RIGSTER_BEAT_ACTION);
									register1.putExtra("result","1");
									sendBroadcast(register1);
									//powerCode = message.substring(32,42);
									if (mHandler != null) {
										mHandler.removeCallbacks(rigsterRunnable);
										mHandler.post(checkRunnable);
									}
									break;
								case 20:
									//鉴权成功
									Intent check = new Intent(CHECK_BEAT_ACTION);
									sendBroadcast(check);
									if (mHandler != null) {
										mHandler.removeCallbacks(checkRunnable);
										mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
									}
									break;
								case 21:
									//鉴权失败,重新注册
									if (mHandler != null) {
										mHandler.removeCallbacks(checkRunnable);
										mHandler.postDelayed(rigsterRunnable, RIGSTER_BEAT_RATE);
										//mHandler.post(rigsterRunnable);
									}
									break;
								case 30:
									//正常心跳
									Intent heartBeat= new Intent(HEART_BEAT_ACTION);
									sendBroadcast(heartBeat);
									break;
								case 40:
									Intent locationUpload= new Intent(LOCATION_UPLOAD_ACTION);
									sendBroadcast(locationUpload);
									//位置信息汇报成功
									if (mHandler != null) {
										mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
									}
									break;
								case 60:
									textMsg = mMsgDecoder.toServerTextMsgBody(mPackageData);
									mHandler.post(terminalComRespRunnable);
									Log.e(TAG,"收到短消息:" + textMsg.getText());
									//准备数据库，存取聊天记录
									openHelper=new ChatSQLiteHelper(getApplicationContext(),"chat.db",null,1) ;
									SQLiteDatabase db=openHelper.getReadableDatabase();
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
									String date = df.format(new Date());
									ContentValues values = new ContentValues();
									values.put("user_id", Constant.myuser_id);
									values.put("contentChat",textMsg.getText());
									values.put("typeChat","text");
									values.put("postdateChat",date);
									values.put("isreadChat",0);
									values.put("ismineChat",0);
									db.insert("chat",null,values);
									db.close();
									Intent text = new Intent(TEXT_ISSUE_ACTION);
									text.putExtra("result",textMsg.getFlag()+","+textMsg.getText());
									sendBroadcast(text);
									break;
								default:
									// 其他消息回复
									/*Intent intent = new Intent(MESSAGE_ACTION);
									intent.putExtra("message", message);
									sendBroadcast(intent);*/
									break;
							}
						} 
					}

				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG,"接收失败");
				}
				finally {
					try {
						is.close();
					}catch (IOException e) {
						e.printStackTrace();
						Log.e(TAG,"关闭失败");
					}

				}

			}
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (mHandler !=null) {
			mHandler.removeMessages(0);
			mHandler = null;
		}
		Message mMessage =new Message();
		mMessage.what=0x003;
		mMyHandler.sendMessage(mMessage);
		if (mReadThread != null) {
			mReadThread.release();
			releaseLastSocket(mSocket);
		}
		return super.onUnbind(intent);
	}
}
