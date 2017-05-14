package com.diablo.dt.diablo.fragment.printer;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.BlueToothPrinter;
import com.diablo.dt.diablo.jolimark.model.ErrorOrMsg;
import com.diablo.dt.diablo.jolimark.model.Event;
import com.diablo.dt.diablo.jolimark.model.PrintContent;
import com.diablo.dt.diablo.jolimark.model.PrinterManager;
import com.diablo.dt.diablo.utils.DiabloDBManager;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.jolimark.printerlib.VAR.TransType;

import de.greenrobot.event.EventBus;

import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlueToothJolimarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlueToothJolimarkFragment extends Fragment implements View.OnClickListener{

    private static final int REQUEST_CONNECT_DEVICE = 0x122;
    private static final int REQUEST_ENABLE_BT = 0x124;
    private static final DiabloUtils UTILS = DiabloUtils.instance();
    // private MyApplication myapplication;
    private Activity activity = null;
    private Button btn_bluetooth_search;
    private Button btn_bluetooth_test;
    private ListView lv_paired_devices, lv_new_devices;
    private TextView tv_connected_dev;
    private ProgressDialog m_pDialog;
    private String btdAddress = null;
    private static String btdAddress_all = null;

    private PrinterManager printerManager = null;

    private BluetoothAdapter btAdapter; // 蓝牙适配器
    private ArrayAdapter<String> pairedDevicesArrayAdapters; // 已经搜索到的设备列表
    private ArrayAdapter<String> newDevicesArrayAdapters; // 搜索到的新设备列表

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x100;

    public BlueToothJolimarkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlueToothJolimarkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlueToothJolimarkFragment newInstance(String param1, String param2) {
        BlueToothJolimarkFragment fragment = new BlueToothJolimarkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = getActivity();
//        myapplication =  getActivity().getApplication();
//        printerManager = myapplication.getPrinterManager();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注册EventBus
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        printerManager = PrinterManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blue_tooth_jolimark, container, false);

        lv_paired_devices = (ListView) view.findViewById(R.id.lv_paired_devices);
        lv_new_devices = (ListView) view.findViewById(R.id.lv_new_devices);
        tv_connected_dev = (TextView) view.findViewById(R.id.tv_connected_dev);
        btn_bluetooth_search = (Button) view.findViewById(R.id.btn_bluetooth_search);
        btn_bluetooth_test = (Button) view.findViewById(R.id.btn_print_test);

        initView();

        return view;
    }

    /**
     * 初始化
     */
    private void initView() {
        // 创建连接进度对话框
        m_pDialog = new ProgressDialog(activity);
        m_pDialog.setMessage(getResources().getString(R.string.connecting));
        m_pDialog.setIndeterminate(false);
        m_pDialog.setCanceledOnTouchOutside(false);

        btn_bluetooth_search.setOnClickListener(this);// 搜索按钮
        btn_bluetooth_test.setOnClickListener(this);

        pairedDevicesArrayAdapters = new ArrayAdapter<>(activity, R.layout.item_device);
        newDevicesArrayAdapters = new ArrayAdapter<>(activity, R.layout.item_device);

        lv_paired_devices.setAdapter(pairedDevicesArrayAdapters);
        lv_paired_devices.setOnItemClickListener(mDeviceClickListener);

        lv_new_devices.setAdapter(newDevicesArrayAdapters);
        lv_new_devices.setOnItemClickListener(mDeviceClickListener);

        // 注册广播接收者
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(mReceiver, filter);

        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(mReceiver, filter2);
        // 初始化蓝牙适配器
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            UTILS.makeToast(activity, R.string.bt_not_available);
            return;
        }
        // 如果没打开蓝牙，提示打开
        if (!btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
//			getBluetoothPermission();
            Bt_ReadyForScan();
        }
        // 初始化界面显示信息
        if (printerManager.isPrinterReady()) {
            TransType ty = printerManager.getTransType();
            if (ty == TransType.TRANS_BT) {
                // String path = printerManager.getAddress();
                if(printerManager.isConnected()){
                    String connectDevice = getString(R.string.bt_connected_device) + btdAddress_all;
                    tv_connected_dev.setText(connectDevice);
                }else{
                    String connectDevice = getString(R.string.bt_prepared_but_no_connected) + btdAddress_all;
                    tv_connected_dev.setText(connectDevice);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        // 销毁页面之前销毁广播接收者
        activity.unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bluetooth_search: // 搜索蓝牙设备
                if (btAdapter.isDiscovering()) {
                    btAdapter.cancelDiscovery(); // 结束上次的搜索
                }
                btAdapter.startDiscovery();
                break;
            case R.id.btn_print_test:
                printerManager.sendData(PrintContent.getFormByteData(getContext()), getContext());
                // PrintContent.createPDFFile(getContext());
                break;
            default:
                break;
        }
    }

    /**
     * 广播接收者
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    newDevicesArrayAdapters.add("Name："+device.getName() + "\nMac：" + device.getAddress()); // 添加找到的蓝牙设备
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (newDevicesArrayAdapters.getCount() == 0) {
                    String noDevices = getResources().getString(R.string.no_found_bt);
                    newDevicesArrayAdapters.add(noDevices);
                }
            }
        }
    };

    /**
     * 选择监听
     */
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            btAdapter.cancelDiscovery();// 取消搜索

            btdAddress_all = ((TextView) v).getText().toString();
            if (btdAddress_all.equals(getResources().getString(R.string.no_found_bt))
                || btdAddress_all.equals(getResources().getString(R.string.prepare_bt))) {
                return;
            }
            btdAddress = btdAddress_all.substring(btdAddress_all.length() - 17);


            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(getString(R.string.select_connect_type));
            builder.setMessage(getString(R.string.connect_dialog_message));
//            builder.setPositiveButton(getString(R.string.open), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    printerManager.setOpenTwoWayFlag(true);
//                    btConnect(btdAddress);
//                }
//            });
            builder.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    printerManager.setOpenTwoWayFlag(false);
                    btConnect(btdAddress);
                }
            });
            builder.show();
        }
    };

    private void btConnect(String btdAddress){
        m_pDialog.show();
        // 初始化printerManager的连接信息
        printerManager.initRemotePrinter(TransType.TRANS_BT,btdAddress);
        new Thread(){
            @Override
            public void run(){
                printerManager.connect();
            }
        }.start();
    }

    // 接收连接事件信息
    public void onEventMainThread(Event connectEvent) {
        m_pDialog.dismiss();
        switch (connectEvent.msg) {
            case ErrorOrMsg.CONNECT_SUCCESS:
                Log.d("tag", "-------------------------连接成功");
                String s = getString(R.string.bt_connected_device)+ btdAddress_all;
                tv_connected_dev.setText(s);
                UTILS.makeToast(activity, R.string.test_connect_success);
                // store
                BlueToothPrinter printer = DiabloDBManager.instance().getBlueToothPrinter();
                String mac = btdAddress_all.substring(btdAddress_all.length() - 17);
                if (DiabloEnum.EMPTY_STRING.equals(printer.getMac())) {
                    DiabloDBManager.instance().addBlueToothPrinter(DiabloEnum.PRINTER_JOLIMARK, mac);
                } else {
                    DiabloDBManager.instance().replaceBlueToothPrinter(DiabloEnum.PRINTER_JOLIMARK, mac);
                }
                printerManager.close();
                break;
            case ErrorOrMsg.CONNECT_FAILED:
                Log.d("tag", "-------------------------连接失败");
                tv_connected_dev.setText(getString(R.string.bt_no_connected_device));
                UTILS.makeToast(activity, R.string.connect_failed);
                printerManager.resetConfig();
                break;
            case ErrorOrMsg.CONNECT_EXIST:
                Log.d("tag", "-------------------------已连接");
                UTILS.makeToast(activity, R.string.connect_exist);
                break;
            case ErrorOrMsg.CONFIG_NULL:
                Log.d("tag", "-------------------------空");
                UTILS.makeToast(activity, R.string.config_null);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    // 可以连接设备
                }
                break;
            case REQUEST_ENABLE_BT: // 打开蓝牙成功
                if (resultCode == Activity.RESULT_OK) {
                    UTILS.makeToast(activity, R.string.open_bt_success);
//				getBluetoothPermission();
                    Bt_ReadyForScan();// 打开蓝牙开关成功后扫描设备前准备操作
                } else {
                    UTILS.makeToast(activity, R.string.open_bt_failed);
                }
        }
    }
    /**
     * 申请权限(android 6.0后蓝牙需要动态申请此权限)
     *
     */
    private void getBluetoothPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            //是否需要向用户解释,默认不需要
            if (showRequest()) return;
            ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    private boolean showRequest(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)){
            //显示一个对话框解释
            new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.access_location_info))
                .setMessage(getString(R.string.location_permission_introduction))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_COARSE_LOCATION_REQUEST_CODE);
                    }
                })
                .setNegativeButton(getString(R.string.cancel),null)
                .create().show();
            return true;
        }
        return false;
    }

    /**
     * 获取已匹配蓝牙设备列表
     */
    private void Bt_ReadyForScan() {
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapters.add("Name："+device.getName() + "\nMac：" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getString(R.string.bt_no_prepare);
            pairedDevicesArrayAdapters.add(noDevices);
        }
    }

}
