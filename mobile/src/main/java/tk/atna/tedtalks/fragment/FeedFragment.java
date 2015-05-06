package tk.atna.tedtalks.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import tk.atna.tedtalks.stuff.ContentManager;
import tk.atna.tedtalks.stuff.LocalBroadcaster;
import tk.atna.tedtalks.R;
import tk.atna.tedtalks.adapter.TalksCursorAdapter;
import tk.atna.tedtalks.provider.TalksContract;

public class FeedFragment extends BaseFragment
                          implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = FeedFragment.class.getSimpleName();

    public static final int TITLE = R.string.talks_feed;

    public static final int FEED_CURSOR_LOADER = 0x00000cc1;

    private ContentManager contentManager = ContentManager.get();

    private TalksCursorAdapter adapter;

    @InjectView(R.id.feed_list)
    ListView feedList;

    // current list position
    private int currItem;

    // flag to prevent multiple loading simultaneously
    private boolean isLoading = false;


    /**
     * Initializes FeedFragment
     *
     * @return instance of retained FeedFragment class
     */
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.inject(this, view);

        if(adapter == null)
            adapter = new TalksCursorAdapter(inflater.getContext(), null, contentManager) {
                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    super.bindView(view, context, cursor);

                    // load next page
                    int position = cursor.getPosition();
                    if(position > 0 && position > cursor.getCount() - 3 && !isLoading) {
                        contentManager.getFeedEarlier(getCount());
                        isLoading = true;
                    }
                }
            };

        feedList.setAdapter(adapter);
        feedList.setSelection(currItem);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initializes loader manager for cursor
        if(getActivity() != null)
            getActivity().getSupportLoaderManager()
                         .initLoader(FEED_CURSOR_LOADER, null, this);

        // refresh list at start
        if(savedInstanceState == null)
            contentManager.getFeed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remember list position
        currItem = feedList.getFirstVisiblePosition();
    }

    @OnItemClick(R.id.feed_list)
    void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // calls details fragment
        Bundle data = new Bundle();
        data.putInt(TALK_ID, (int) id);
        makeFragmentAction(ACTION_TALK_DETAILS, data);
    }

    /**
     * LocalBroadcaster callback to catch and process commands (with data)
     *
     * @param action action to process
     * @param data received data
     */
    @Override
    public void onReceive(int action, Bundle data) {
        switch (action) {
            // feed loaded
            case LocalBroadcaster.ACTION_REFRESH_FEED:
                isLoading = false;
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                                TalksContract.Talks.CONTENT_URI,
                                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == FEED_CURSOR_LOADER) {
            adapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.changeCursor(null);
    }

}
