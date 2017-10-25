package com.ditagis.hcm.docsotanhoa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.Calendar;

import static com.ditagis.hcm.docsotanhoa.R.id.container;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int mKy;
    private int mNam;
    private int mDot;
    private String mUsername, mStaffName;

    private LayLoTrinh mLayLoTrinh;
    private DocSo mDocSo;
    private QuanLyDocSo mQuanLyDocSo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        this.mKy = calendar.get(Calendar.MONTH) + 1;
        this.mNam = calendar.get(Calendar.YEAR);
        if (getIntent().getExtras().getString("mayds") != null)
            this.mUsername = getIntent().getExtras().getString("mayds");
        if (getIntent().getExtras().getString("staffname") != null)
            this.mStaffName = getIntent().getExtras().getString("staffname");
        if (getIntent().getExtras().getInt("dot") > 0)
            this.mDot = getIntent().getExtras().getInt("dot");

        mLayLoTrinh = new LayLoTrinh(getLayoutInflater(), mKy, mNam, mDot, mUsername, mStaffName);
        mDocSo = new DocSo(getLayoutInflater(), mKy, mDot, mUsername);
        mQuanLyDocSo = new QuanLyDocSo(getLayoutInflater(), mDot, mUsername);
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
                    case 0:
                        mLayLoTrinh.setTextProgress();
                        break;
                    case 1:
                        mDocSo.setTextProgress();
                        break;
                    case 2:
                        mQuanLyDocSo.refresh();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
        if (id == R.id.action_log_out) {
            String password = loadPreferences(this.mUsername);
            deletePreferences(this.mUsername);
            deletePreferences(password);
            deletePreferences(this.mStaffName);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public SharedPreferences getPreferences() {
        return getSharedPreferences("LOGGED_IN", MODE_PRIVATE);
    }

    public boolean deletePreferences(String key) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.remove(key).commit();
        return false;
    }

    public String loadPreferences(String key) {
        try {
            SharedPreferences sharedPreferences = getPreferences();
            String strSavedMemo = sharedPreferences.getString(key, "");
            return strSavedMemo;
        } catch (NullPointerException nullPointerException) {
            return null;
        }
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
                case 0:
                    return mLayLoTrinh;
                case 1:
                    return mDocSo;
                case 2:
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
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Lấy lộ trình";
                case 1:
                    return "Đọc số";
                case 2:
                    return "Quản lý đọc số";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
