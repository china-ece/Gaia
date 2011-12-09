package com.chinaece.gaia.util;

import android.os.Handler;
import android.os.Message;

public class NetworkManagerAsync {
	private static final int MsgConnect= 0x10001;
	private static final int MsgComplete = 0x10002;
	private static final int MsgError = 0x10003;
	//Message Handler 
	private  EventHandler mHandler = new EventHandler(this);
	
	//Listener
	public interface onConnectListener{
		void onConnect(NetworkManagerAsync mManager);
	}
	private onConnectListener mOnConnectListener;
	public void setmOnConnectListener(onConnectListener mOnConnectListener) {
		this.mOnConnectListener = mOnConnectListener;
	}

	public interface onCompleteListener{
		void onComplete(NetworkManagerAsync mManager, Object result);
	}
	private onCompleteListener mOnCompleteListener;
	public void setmOnCompleteListener(onCompleteListener mOnCompleteListener) {
		this.mOnCompleteListener = mOnCompleteListener;
	}
	
	public interface onErrorListener{
		void onError(NetworkManagerAsync mManager, Exception e);
	}
	private onErrorListener mOnErrorListener;
	public void setmOnErrorListener(onErrorListener mOnErrorListener) {
		this.mOnErrorListener = mOnErrorListener;
	}
	
	//send message
	public void sendMessage(int what){
		mHandler.sendMessage(mHandler.obtainMessage(what));
	}
	
	public void sendMessage(int what, Object result){
		mHandler.sendMessage(mHandler.obtainMessage(what, result));
	}
	
	//Message Handler  Implement
    private class EventHandler extends Handler {
        private NetworkManagerAsync mManager;  
  
        public EventHandler(NetworkManagerAsync manager) {  
            mManager = manager;  
        }  
  
        // dispatch Message  
        @Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case MsgConnect:
                if (mOnConnectListener != null)  
                	mOnConnectListener.onConnect(mManager);  
                break;  
            case MsgComplete:
                if (mOnCompleteListener != null)  
                	mOnCompleteListener.onComplete(mManager, msg.obj);  
                break;  
            case MsgError:
                if (mOnErrorListener != null)  
                	mOnErrorListener.onError(mManager,  (Exception) msg.obj);  
                break;  
            default:  
                break;  
            }  
        }  
    }  
}
