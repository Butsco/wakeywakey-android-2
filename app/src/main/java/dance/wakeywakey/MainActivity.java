package dance.wakeywakey;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends FragmentActivity {
    public static final int PAGE_SET_ALARM = 0;
    public static final int PAGE_SELECT_CONTACT = 1;
    public static final int PAGE_SELECT_ASSIGNMENT = 2;
    public static final int PAGE_COMPLETE = 3;

    public static final boolean AUTO_SLIDE = false;

    private HashMap<String, String> postData = new HashMap<String, String>();
    private String postUrl = "http://wakey-env.elasticbeanstalk.com/v1/alarms/?access_token=wham";

    private static final int NUM_PAGES = 4;
    public ViewPagerCustomDuration viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private int NOTIFICATION = R.string.app_name;

    private boolean isSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPagerCustomDuration) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setOffscreenPageLimit(NUM_PAGES - 1);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setScrollDurationFactor(3);
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            viewPager.setScrollDurationFactor(1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            switch (position) {
                case PAGE_SET_ALARM:
                    fragment = new SetAlarmFragment();
                    break;
                case PAGE_SELECT_CONTACT:
                    fragment = new SelectContactFragment();
                    break;
                case PAGE_SELECT_ASSIGNMENT:
                    fragment = new SelectAssignmentFragment();
                    break;
                case PAGE_COMPLETE:
                    fragment = new CompleteFragment();
                    break;
                default:
                    return null;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    private AsyncTask<Void, Void, Void> sendTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
            byte[] result = null;
            String str = "";
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postUrl);// in this case, params[0] is URL
            try {
                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                Iterator<String> it = postData.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    nameValuePair.add(new BasicNameValuePair(key, postData.get(key)));
                }

                post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
                HttpResponse response = client.execute(post);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(Constants.TAG, "response: " + str);

            return null;
        }
    };

    public void addDataToPost(String key, String value) {
        postData.put(key, value);
    }

    public String getData(String key) {
        if (postData.containsKey(key)) {
            return postData.get(key);
        }
        return "";
    }

    public void sendPost() {
        addDataToPost("from", "32474418798");
        addDataToPost("fromName", "Bert");
        addDataToPost("mood", "happy");

        if (!isSent) {
            isSent = true;

            sendTask.execute(null, null, null);

            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getCompleteMessage())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .build();

            // Send the notification.
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION, notification);
        }
    }

    public void nextSlideWithDelay() {
        if (AUTO_SLIDE) {
            final Handler h = new Handler();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    viewPager.setScrollDurationFactor(3);
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    viewPager.setScrollDurationFactor(1);
                }
            };

            h.postDelayed(r, 1000); // 1 second delay
        }
    }

    public String getCompleteMessage() {
        String name = getData("toFirstName");

        if (name.equals("")) {
            name = getData("toName");
        }

        String time = getData("timeString");
        return String.format(getString(R.string.complete_message), name, time);
    }

    @Override
    protected void onDestroy() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION);

        super.onDestroy();
    }
}
