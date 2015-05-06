package tk.atna.tedtalks.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayDeque;

import tk.atna.tedtalks.R;
import tk.atna.tedtalks.stuff.Utils;
import tk.atna.tedtalks.fragment.BaseFragment;
import tk.atna.tedtalks.fragment.DetailsFragment;
import tk.atna.tedtalks.fragment.FeedFragment;


public class TalksActivity extends ActionBarActivity
                           implements BaseFragment.FragmentActionCallback,
                                      FragmentManager.OnBackStackChangedListener {

    private static final String TITLE_STACK = "title";
    private static final String BACKSTACK = "backstack";

    private ArrayDeque<String> titleStack = new ArrayDeque<>();
    private boolean backstackEmpty = true;

    ActionBar actionbar;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talks);

        if(savedInstanceState != null) {
            titleStack = (ArrayDeque<String>) savedInstanceState.getSerializable(TITLE_STACK);
            backstackEmpty = savedInstanceState.getBoolean(BACKSTACK);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionbar = getSupportActionBar();
        actionbar.setTitle(titleStack.peek());
        actionbar.setDisplayHomeAsUpEnabled(!backstackEmpty);


        // shadow under toolbar on new devices
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View shadow = findViewById(R.id.shadow_prelollipop);
            shadow.setVisibility(View.GONE);
            toolbar.setElevation(8);
        }

        if (savedInstanceState == null) {
            Utils.parkFragment(getSupportFragmentManager(),
                    R.id.content,
                    FeedFragment.class,
                    null, false);
            titleStack.push(getString(FeedFragment.TITLE));
            actionbar.setTitle(titleStack.peek());
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(TITLE_STACK, titleStack);
        outState.putBoolean(BACKSTACK, backstackEmpty);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
    }

    @Override
    public void onAction(int action, Bundle data) {
        switch (action) {
            case BaseFragment.ACTION_FINISH:
                onBackPressed();
                break;

            case BaseFragment.ACTION_TALK_DETAILS:
                Utils.parkFragment(getSupportFragmentManager(),
                                    R.id.content,
                                    DetailsFragment.class,
                                    data, true);
                titleStack.push(getString(DetailsFragment.TITLE));
                actionbar.setTitle(titleStack.peek());
                break;
        }
    }

    @Override
    public void onBackStackChanged() {
        backstackEmpty = getSupportFragmentManager().getBackStackEntryCount() == 0;
        // show back arrow
        actionbar.setDisplayHomeAsUpEnabled(!backstackEmpty);
        // refresh menu items
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().popBackStackImmediate()) {
            titleStack.pop();
            actionbar.setTitle(titleStack.peek());
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return super.onSupportNavigateUp();
    }

}
