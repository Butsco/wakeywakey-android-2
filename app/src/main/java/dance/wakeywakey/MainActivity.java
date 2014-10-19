package dance.wakeywakey;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
    private HashMap<String, String> postData = new HashMap<String, String>();
    private String postUrl = "http://wakey-env.elasticbeanstalk.com/v1/alarms/?access_token=wham";

    private static final int NUM_PAGES = 4;
    private ViewPagerCustomDuration viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPagerCustomDuration) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setOffscreenPageLimit(NUM_PAGES-1);
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
                case 0:
                    fragment = new SetAlarmFragment();
                    break;
                case 1:
                    fragment = new SelectContactFragment();
                    break;
                case 2:
                    fragment = new SelectAssignmentFragment();
                    break;
                case 3:
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
                if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                }
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
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

        sendTask.execute(null, null, null);
    }

    public void nextSlideWithDelay() {
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
