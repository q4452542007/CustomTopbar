package mapsoft.com.costomtopbar.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.secondbook.com.buttonfragment.R;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mapsoft.com.costomtopbar.adapter.ChatMsgViewAdapter;
import mapsoft.com.costomtopbar.constant.ChatMsgEntity;
import mapsoft.com.costomtopbar.constant.Constant;
import mapsoft.com.costomtopbar.db.ChatSQLiteHelper;

/**
 * @author djl
 * @function
 */

public class MessageActivity extends BaseActivity{

    ChatSQLiteHelper openHelper;
    private ChatMsgViewAdapter mAdapter;
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();

    private EditText mEditTextContent;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_layout);
        initView();
        //1、从本地sqlite数据库获取历史数据
        //首次进入，先把聊天记录调出
        initdb();
        readChat();
    }

    private void chatContent() {
    }

    //初始化界面
    public void initView() {
        mListView = (ListView)findViewById(R.id.listview);
    }
    private void initdb()
    {
        //准备数据库，存取聊天记录
        openHelper=new ChatSQLiteHelper(this,"chat.db",null,1) ;
    }
    private void readChat(){
        String _date;
        String _text;
        String _isreadChat;
        String _ismineChat; //0：对方的消息  1：自己发送的消息
        boolean _isComMeg = true;
        //获取数据库中的信息
        SQLiteDatabase db=openHelper.getReadableDatabase();
        String sql="select contentChat,postdateChat,isreadChat,ismineChat from chat where user_id=?";
        Cursor c = db.rawQuery(sql,new String[]{Constant.myuser_id});
        while(c.moveToNext()){
            _text=c.getString(0);
            _date=c.getString(1);
            _isreadChat=c.getString(2);
            _ismineChat=c.getString(3);
            Log.v("ceshi", _text+_date+_isreadChat);
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setText(_text);
            entity.setData(_date);
            if(_ismineChat!=null && _ismineChat.equals("0")){_isComMeg= false;}
            entity.setMsgType(_isComMeg);
            mDataArrays.add(entity);
        }
        mAdapter = new ChatMsgViewAdapter(this,mDataArrays);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mListView.getCount() - 1);
        db.close();
    }
}
