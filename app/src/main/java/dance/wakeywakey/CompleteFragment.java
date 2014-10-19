package dance.wakeywakey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by bert on 19/10/14.
 */
public class CompleteFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private TextView completeMessageTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_complete, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completeMessageTextView = (TextView) view.findViewById(R.id.complete_message);

        ((MainActivity) getActivity()).viewPager.setOnPageChangeListener(this);
    }

    public void onPageSelected() {


    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == MainActivity.PAGE_COMPLETE) {
            MainActivity mainActivity = (MainActivity) getActivity();
            String message = mainActivity.getCompleteMessage();
            CompleteFragment.this.completeMessageTextView.setText(message);

            mainActivity.sendPost();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
