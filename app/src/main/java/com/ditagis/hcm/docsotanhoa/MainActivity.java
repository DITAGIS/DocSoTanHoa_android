package com.ditagis.hcm.docsotanhoa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.receiver.NetworkStateChangeReceiver;
import com.ditagis.hcm.docsotanhoa.theme.ThemeUtils;
import com.ditagis.hcm.docsotanhoa.utities.ChangePassword;
import com.ditagis.hcm.docsotanhoa.utities.DialogSelectDot;
import com.ditagis.hcm.docsotanhoa.utities.Flag;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;
import com.ditagis.hcm.docsotanhoa.utities.NUMBER;
import com.ditagis.hcm.docsotanhoa.utities.Preference;
import com.ditagis.hcm.docsotanhoa.utities.Printer1;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.ditagis.hcm.docsotanhoa.R.id.container;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    protected static final String TAG = "TAG";
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mBluetoothDevice;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket;
    private ViewPager mViewPager;
    private DocSo mDocSo;
    private QuanLyDocSo mQuanLyDocSo;
    private NetworkStateChangeReceiver mStateChangeReceiver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            mBluetoothConnectProgressDialog.dismiss();
//            Toast.makeText(MainActivity.this.getApplicationContext(), "Đã kết nối", Toast.LENGTH_SHORT).show();
        }
    };


    //bluetooth
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    public DocSo getmDocSo() {
        return mDocSo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.Theme_AppCompat_DayNight);
        Calendar calendar = Calendar.getInstance();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
//                    case 0:
//                        mLayLoTrinh.setTextProgress();
//                        break;
                    case 0:
//                        mDocSo.setmDot(mLayLoTrinh.getmDo  if (mDocSo.checkDotExist())
                        mDocSo.refresh();
//                        mDocSo.selectDotFromOut(mQuanLyDocSo.getmDot());
//                        DialogSelectDot.show(MainActivity.this, mDot, mKy, mNam, mUsername, mDocSo);
                        break;
                    case 1:
//                        mQuanLyDocSo.setmDot(mLayLoTrinh.getmDot());
//                        mQuanLyDocSo.getmUploading().setmDot(mLayLoTrinh.getmDot());

                        mQuanLyDocSo.refresh();
//                        mQuanLyDocSo.selectDotFromOut(mDocSo.getmDot());
                        DialogSelectDot.show(MainActivity.this, mQuanLyDocSo);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mDocSo = new DocSo(getLayoutInflater(), mViewPager);

        mQuanLyDocSo = new QuanLyDocSo(getLayoutInflater());

        DialogSelectDot.show(MainActivity.this, mDocSo);
//        new Initialization().execute();
//        mStateChangeReceiver = new NetworkStateChangeReceiver(tabLayout, MainActivity.this);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        intentFilter.addAction("android.net.conn.WIFI_STATE_CHANGED");
//        registerReceiver(mStateChangeReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        try {
            if (mStateChangeReceiver != null)
                unregisterReceiver(mStateChangeReceiver);
        } catch (IllegalArgumentException e) {
            mStateChangeReceiver = null;
        }
        super.onDestroy();
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

        //noinspection SimplifiableIfStatement
        switch (id) {


            case R.id.action_log_out:
//            deletePreferences();

                finish();
                return true;
            case R.id.action_change_theme:
                optionChangeUITheme();
                return true;
            case R.id.action_change_password:

                new ChangePassword(MainActivity.this.getApplicationContext(), MainActivity.this);
                return true;
            case R.id.action_connect_bluetooth:
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(MainActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                NUMBER.REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(MainActivity.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent,
                                NUMBER.REQUEST_CONNECT_DEVICE);
                    }
                }

                return true;
            case R.id.action_select_folder:
                DialogSelectDot.show(MainActivity.this, mDocSo);

                return true;
//            case R.id.action_fix_not_update:
//                String like = String.format("%02d",mQuanLyDocSo.getmDot())
//                        + Preference.getInstance().loadPreference(getString(R.string.preference_username)) + "%";
//                int ky = mQuanLyDocSo.getmKy();
//                List<HoaDon> hoaDons = LocalDatabase.getInstance(this).getAllHoaDon(like, ky);
//                for (HoaDon hoaDon : hoaDons) {
//                    if (hoaDon.getCodeMoi() == null)
//                        continue;
//                    else if (hoaDon.getCodeMoi().startsWith("F"))
//                        LocalDatabase.getInstance(this).updateHoaDonFlag(hoaDon, Flag.CODE_F);
//                    else
//                        LocalDatabase.getInstance(this).updateHoaDonFlag(hoaDon, Flag.READ);
//                }
//                return true;
//            case R.id.action_fix_no_consume:
//                fix_no_consumn();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    private void optionChangeUITheme() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Chọn màu nền");
        builder.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_theme, null);

        final RadioGroup group = (RadioGroup) dialogLayout.findViewById(R.id.radioGroup_select_theme);
        if (loadPreferences(getString(R.string.save_theme)) == 0 || loadPreferences(getString(R.string.save_theme)) == ThemeUtils.THEME_DEFAULT)
            group.check(R.id.radio_theme_1);
        else
            group.check(R.id.radio_theme_2);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int iChecked = group.getCheckedRadioButtonId();
                switch (iChecked) {
                    case R.id.radio_theme_1:
                        savePreferences(getString(R.string.save_theme), ThemeUtils.THEME_DEFAULT);
                        mDocSo.setmSelected_theme(ThemeUtils.THEME_DEFAULT);
                        mQuanLyDocSo.setmSelected_theme(ThemeUtils.THEME_DEFAULT);
                        break;
                    case R.id.radio_theme_2:
                        savePreferences(getString(R.string.save_theme), ThemeUtils.THEME_DARK);
                        mDocSo.setmSelected_theme(ThemeUtils.THEME_DARK);
                        mQuanLyDocSo.setmSelected_theme(ThemeUtils.THEME_DARK);
                        break;
                }
                mDocSo.refresh();
                mQuanLyDocSo.refresh();
//                mSearchType = spinSearchType.getSelectedItem().toString();
                dialog.dismiss();
            }
        });
        builder.setView(dialogLayout);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void fix_no_consumn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Sửa lỗi không có tiêu thụ");
        builder.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.layout_edittext, null);
        final EditText editText = dialogLayout.findViewById(R.id.edit_layout_edittext);
        dialogLayout.findViewById(R.id.txt_layout_edittext).setVisibility(View.VISIBLE);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String like = String.format("%02d",mQuanLyDocSo.getmDot())
                        + Preference.getInstance().loadPreference(getString(R.string.preference_username)) + "%";
                int ky = mQuanLyDocSo.getmKy();
                List<HoaDon> hoaDons = LocalDatabase.getInstance(MainActivity.this).getAllHoaDon(like, ky);
                for (HoaDon hoaDon : hoaDons) {
                    if (hoaDon.getDanhBo() == null)
                        continue;
                    if (hoaDon.getDanhBo().contains(editText.getText().toString())) {
                        LocalDatabase.getInstance(MainActivity.this).updateHoaDonFlag(hoaDon, Flag.UNREAD);
                        break;
                    }
                }
                mDocSo.refresh();
                mQuanLyDocSo.refresh();
                dialog.dismiss();
            }
        });
        builder.setView(dialogLayout);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    public SharedPreferences getPreferences() {
        return getSharedPreferences("LOGGED_IN", MODE_PRIVATE);
    }

    /**
     * Method used to save Preferences
     */
    public void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = getPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Method used to load Preferences
     */
//    public String loadPreference(String key) {
//        try {
//            SharedPreferences sharedPreferences = getPreferences();
//            String strSavedMemo = sharedPreferences.getString(key, "");
//            return strSavedMemo;
//        } catch (NullPointerException nullPointerException) {
//            return null;
//        }
//    }
    public int loadPreferences(String key) {
        try {
            SharedPreferences sharedPreferences = getPreferences();
            int strSavedMemo = sharedPreferences.getInt(key, ThemeUtils.THEME_DEFAULT);
            return strSavedMemo;
        } catch (NullPointerException nullPointerException) {
            return 0;
        }
    }

    /**
     * Method used to delete Preferences
     */
    public boolean deletePreferences(String key) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.remove(key).commit();
        return false;
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
        try {
            switch (mRequestCode) {
                case NUMBER.REQUEST_CONNECT_DEVICE:
                    if (mResultCode == Activity.RESULT_OK) {
                        Bundle mExtra = mDataIntent.getExtras();
                        String mDeviceAddress = mExtra.getString("DeviceAddress");
                        Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                        mBluetoothDevice = mBluetoothAdapter
                                .getRemoteDevice(mDeviceAddress);
//
//                        mBluetoothSocket = mBluetoothDevice
//                                .createRfcommSocketToServiceRecord(applicationUUID);
//                        mBluetoothAdapter.cancelDiscovery();
//                        mBluetoothSocket.connect();
//                        mHandler.sendEmptyMessage(0);
                        new ConnectBluetoothAsycn().execute();
                        // pairToDevice(mBluetoothDevice); This method is replaced by
                        // progress dialog with thread
//                        Printer.getInstance().initialize(mBluetoothDevice, mBluetoothAdapter, MainActivity.this.getApplicationContext());

                    }
                    break;

                case NUMBER.REQUEST_ENABLE_BT:
                    if (mResultCode == Activity.RESULT_OK) {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(MainActivity.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent, NUMBER.REQUEST_CONNECT_DEVICE);
                    } else {
                        Toast.makeText(MainActivity.this, "Message", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } catch (Exception e) {


        }
    }

    @Override
    public void onBackPressed() {
        //do nothing

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mDocSo.setmLastLocation(mDocSo.getmLocationHelper().getLocation());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//
            switch (position) {
//                case 0:
//                    return mLayLoTrinh;
                case 0:
                    return mDocSo;
                case 1:
                    return mQuanLyDocSo;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
//                case 0:
//                    return "Lấy lộ trình";
                case 0:
                    return "Đọc số";
                case 1:
                    return "Quản lý đọc số";
            }
            return null;
        }
    }

    class ConnectBluetoothAsycn extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog dialog;


        public ConnectBluetoothAsycn() {

//            this.dialog = new ProgressDialog(MainActivity.this.getApplicationContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            this.dialog.setTitle("Đang kết nối...");
//            this.dialog.setMessage(mBluetoothDevice.getName() + " : "
//                    + mBluetoothDevice.getAddress());
//            this.dialog.setCancelable(false);
//            this.dialog.show();

//            this.dialog = ProgressDialog.show(MainActivity.this.getApplicationContext(),
//                    "Đang kết nối...", mBluetoothDevice.getName() + " : "
//                            + mBluetoothDevice.getAddress(), true, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mBluetoothSocket = mBluetoothDevice
                        .createRfcommSocketToServiceRecord(applicationUUID);
                mBluetoothAdapter.cancelDiscovery();
                mBluetoothSocket.connect();
                mmOutputStream = mBluetoothSocket.getOutputStream();
                mmInputStream = mBluetoothSocket.getInputStream();

                beginListenForData();
                mHandler.sendEmptyMessage(0);
                return true;
            } catch (Exception e) {
                System.out.print(e.toString());
            }
            return false;
        }

        void beginListenForData() {
            try {
                final Handler handler = new Handler();

                // this is the ASCII code for a newline character
                final byte delimiter = 10;

                stopWorker = false;
                readBufferPosition = 0;
                readBuffer = new byte[1024];

                workerThread = new Thread(new Runnable() {
                    public void run() {

                        while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                            try {

                                int bytesAvailable = mmInputStream.available();

                                if (bytesAvailable > 0) {

                                    byte[] packetBytes = new byte[bytesAvailable];
                                    mmInputStream.read(packetBytes);

                                    for (int i = 0; i < bytesAvailable; i++) {

                                        byte b = packetBytes[i];
                                        if (b == delimiter) {

                                            byte[] encodedBytes = new byte[readBufferPosition];
                                            System.arraycopy(
                                                    readBuffer, 0,
                                                    encodedBytes, 0,
                                                    encodedBytes.length
                                            );

                                            // specify US-ASCII encoding
                                            final String data = new String(encodedBytes, "US-ASCII");
                                            readBufferPosition = 0;

                                            // tell the user data were sent to bluetooth printer device

                                        } else {
                                            readBuffer[readBufferPosition++] = b;
                                        }
                                    }
                                }

                            } catch (IOException ex) {
                                stopWorker = true;
                            }

                        }
                    }
                });

                workerThread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
//                Printer.getInstance().initialize(mBluetoothSocket, MainActivity.this);
                Printer1.getInstance().initialize(mmOutputStream);

//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
                MySnackBar.make(MainActivity.this.mViewPager, "Đã kết nối", true);
            } else {
                MySnackBar.make(MainActivity.this.mViewPager, "Kết nối thất bại. Vui lòng kết nối lại", true);
            }
        }
    }

}
