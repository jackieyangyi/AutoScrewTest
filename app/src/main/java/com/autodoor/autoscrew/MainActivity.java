package com.autodoor.autoscrew;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.autodoor.autoscrew.ActivitySys.ActivitySys;
import com.autodoor.autoscrew.Bluetooth.BluetoothChatService;
import com.autodoor.autoscrew.Bluetooth.Constants;
import com.autodoor.autoscrew.Bluetooth.DeviceListActivity;
import com.autodoor.autoscrew.data.CommandManager;
import com.autodoor.autoscrew.data.CommandSys;
import com.autodoor.autoscrew.dialog.DialogNumber;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DialogNumber.NoticeDialogListener {

    DialogNumber dialogNumber;
    boolean passwordFlag;

    Button btnSys;


    ProgressDialog progress;


    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    //
    Button btnZero;


    //myself
    public static MainActivity mainActivity;
    private String mMsgSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = (EditText) findViewById(R.id.etOutput);
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, InputMethodManager.SHOW_FORCED);


        btnSys = (Button) findViewById(R.id.btnSys);
        Button btnPara = (Button) findViewById(R.id.btnPara);
        Button btnGetScrew = (Button) findViewById(R.id.btnGetScrew);
        Button btnSetScrew = (Button) findViewById(R.id.btnSetScrew);
        View.OnClickListener listener = getOnClickListener();

        btnSys.setOnClickListener(listener);
        btnPara.setOnClickListener(listener);
        btnGetScrew.setOnClickListener(listener);
        btnSetScrew.setOnClickListener(listener);



        /*progress = ProgressDialog.show(this, "请稍候",
                "初始数据中，请稍候...", true);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                // do the thing that takes a long time
                doesDatabaseExist(getApplicationContext(), DatabaseHelper.DB_NAME);

                DatabaseHelper database = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = null;
                db = database.getWritableDatabase();
                db.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });
            }
        }).start();*/

        //蓝牙部分开始
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙设备不可用", Toast.LENGTH_LONG).show();
        }


        //按钮
        btnZero = (Button) findViewById(R.id.btnZero);
        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(v.getContext(), ActivityABLocation.class);
                startActivity(intent2);


                Log.d("test", "宽度高度" + MainActivity.this.getWindow().getDecorView().getWidth() + "==" + MainActivity.this.getWindow().getDecorView().getHeight());


                sendMessage("main","02 80 00 82 ");
            }
        });

        mainActivity = this;
    }


    //判断数据库文件是否存在
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        boolean flag = dbFile.exists();
        Log.d("db path delete :", flag + "--" + dbFile.getAbsolutePath());
        if (flag) {
            dbFile.delete();
        }
        return flag;
    }

    @NonNull
    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    /*case R.id.btnArc:
                        Toast.makeText(v.getContext(),"测试测试",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), ActivityArc.class);
                        startActivity(intent);
                        break;*/
                    case R.id.btnSys:
                        Intent intent2 = new Intent(v.getContext(), ActivitySys.class);
                        startActivity(intent2);

                        if (true) break;

                        //输入密码才能允许进入系统设置
                        btnSys.setEnabled(false);
                        Toast.makeText(v.getContext(), "输入密码才能进行设置", Toast.LENGTH_SHORT).show();
                        passwordFlag = false;//默认没有输入正确密码；
                        CheckPassword();

                        break;
                    case R.id.btnPara:
                        Intent intent3 = new Intent(v.getContext(), ActivityPara.class);
                        startActivity(intent3);
                        break;
                    case R.id.btnGetScrew:
                        Intent intent4 = new Intent(v.getContext(), ActivityGetScrew.class);
                        startActivity(intent4);
                        break;
                    case R.id.btnSetScrew:
                        Intent intent5 = new Intent(v.getContext(), ActivitySetScrew.class);
                        startActivity(intent5);
                        break;
                    default:
                        break;
                }

            }
        };
    }

    private void CheckPassword() {

        dialogNumber = new DialogNumber();
        Bundle args = new Bundle();
        args.putString("dialogType", "password");
        args.putString("number", "");
        dialogNumber.setArguments(args);
        dialogNumber.show(getSupportFragmentManager(), "DialogNumber");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.action_settings: {
                finish();
                return true;
            }
            /*case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }*/
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();


        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }


    @Override
    public void onConfirm(DialogInterface dialogInterface, Bundle bundle) {
        btnSys.setEnabled(true);
        dialogNumber = null;
        //赋值回原来控件
        String temp = bundle.getString("number");
        if (temp.equals("1234")) {
            passwordFlag = true;
        } else {
            passwordFlag = false;
        }

        if (!passwordFlag) {
            Toast.makeText(getApplicationContext(), "密码不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent2 = new Intent(this, ActivitySys.class);
        startActivity(intent2);
    }

    @Override
    public void onCancle(DialogInterface dialogInterface, Bundle bundle) {

        btnSys.setEnabled(true);
        dialogNumber = null;
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface, Bundle bundle) {

        btnSys.setEnabled(true);
        dialogNumber = null;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("BT", "onStart");
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BT", "onDestroy");
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        //mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        //mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        //mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        /*mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });*/

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        //mOutStringBuffer = new StringBuffer("");
    }


    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //readMessage = bytesToHexString(readBuf, 0, msg.arg1);
                    byte[] readByte = new byte[msg.arg1];
                    System.arraycopy(readBuf,0,readByte,0,msg.arg1);
                    readMessage = byteArrayToHexStringWithSpace(readByte);

                    //Toast.makeText(MainActivity.this, readMessage, Toast.LENGTH_SHORT).show();
                    Log.d("收到的消息", readMessage);

                    CommandManager commandManager = new CommandManager();
                    commandManager.GetCommandInfo(readBuf);


                    SendToView(commandManager);


                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != MainActivity.this) {
                        Toast.makeText(MainActivity.this, "连接到 "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != MainActivity.this) {
                        Toast.makeText(MainActivity.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void SendToView(CommandManager commandManager) {
        if (null != ActivitySys.myself && ActivitySys.myself.isDisplaying ) {
            Log.d("to ", "ActivitySys");
            ActivitySys.myself.ShowBTMessage(mMsgSource, commandManager);

        }
        if (null != ActivityPara.myself){Log.d("test  ", "" + ActivityPara.myself.isDisplaying);}
        if (null != ActivityPara.myself && ActivityPara.myself.isDisplaying ) {
            Log.d("to ", "ActivityPara");
            ActivityPara.myself.ShowBTMessage(mMsgSource, commandManager);

        }
        if (null != ActivityGetScrew.myself && ActivityGetScrew.myself.isDisplaying ) {
            Log.d("to ", "ActivityGetScrew");
            ActivityGetScrew.myself.ShowBTMessage(mMsgSource, commandManager);

        }

        if (null != ActivitySetScrew.myself){Log.d("test  ActivitySetScrew", "" + ActivitySetScrew.myself.isDisplaying);}
        if (null != ActivitySetScrew.myself && ActivitySetScrew.myself.isDisplaying ) {
            Log.d("to ", "ActivitySetScrew");
            ActivitySetScrew.myself.ShowBTMessage(mMsgSource, commandManager);

        }
    }


    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        //FragmentActivity activity = getActivity();
        if (null == MainActivity.this) {
            return;
        }
        final ActionBar actionBar = MainActivity.this.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        //FragmentActivity activity = getActivity();
        if (null == MainActivity.this) {
            return;
        }
        final android.support.v7.app.ActionBar actionBar = MainActivity.this.getSupportActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(MainActivity.this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    MainActivity.this.finish();
                }
        }
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }


    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    public void sendMessage(String msgSource,String message) {
        mMsgSource = msgSource;

        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = hexStringToByteArray(message);
            mChatService.write(send);

            Log.d("发送了信息", message);
            // Reset out string buffer to zero and clear the edit text field
            //mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
        }
    }


    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    /*private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };*/
    public static String bytesToHexString(byte[] src, int start, int num) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = start; i < num; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    public static byte[] hexStringToByteArray(String s) {
        s = s.replace(" ", "");
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    final protected static char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;

        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static String byteArrayToHexStringWithSpace(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        int v;

        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }

        return new String(hexChars);
    }

}
