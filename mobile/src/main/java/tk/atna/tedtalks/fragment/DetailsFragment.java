package tk.atna.tedtalks.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.atna.tedtalks.stuff.ContentManager;
import tk.atna.tedtalks.R;
import tk.atna.tedtalks.model.Talk;

public class DetailsFragment extends BaseFragment {

    public static final String TAG = DetailsFragment.class.getSimpleName();

    public static final int TITLE = R.string.talk_details;

    private ContentManager contentManager = ContentManager.get();

    private Talk talk;

    private MediaController mediaController;
    private int playPosition;

    @InjectView(R.id.vv_talk_video)
    VideoView vvTalkVideo;

    @InjectView(R.id.tv_name)
    TextView tvName;

    @InjectView(R.id.tv_desc)
    TextView tvDesc;

    @InjectView(R.id.tv_published)
    TextView tvPublished;

    @InjectView(R.id.tv_recorded)
    TextView tvRecorded;

    @InjectView(R.id.tv_updated)
    TextView tvUpdated;

    @InjectView(R.id.tv_viewed)
    TextView tvViewed;

    @InjectView(R.id.tv_commented)
    TextView tvCommented;

    @InjectView(R.id.tv_emailed)
    TextView tvEmailed;


    /**
     * Initializes DetailsFragment with data
     *
     * @param data fragment initialize data
     * @return instance of retained DetailsFragment class
     */
    public static DetailsFragment newInstance(Bundle data) {
        DetailsFragment fragment = new DetailsFragment();
        fragment.setRetainInstance(true);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // process init arguments
        if(getArguments() != null) {
            // seeking for id
            int id = getArguments().getInt(TALK_ID);
            // no talk id - finish
            if(id == 0) {
                makeFragmentAction(ACTION_FINISH, null);
                Log.d("myLogs", "No talk to play");

                if(getActivity() != null)
                    Toast.makeText(getActivity(), "Can't find details for this talk",
                                    Toast.LENGTH_LONG).show();
                return;
            }
            // remember id
            talk = new Talk(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.inject(this, view);

        // sets video view size as ~3/4 format
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (width * 0.7f);
        vvTalkVideo.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // prepares media controller
        initMediaController();

        // pulls talk data from provider
        pullTalk();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remember list position
        playPosition = vvTalkVideo.getCurrentPosition();
    }

    /**
     * LocalBroadcaster callback to catch and process commands (with data)
     *
     * @param action action to process
     * @param data received data
     */
    @Override
    public void onReceive(int action, Bundle data) {
        // nothing to catch
    }

    /**
     * Pulls talk from sqlite and populates views with its data
     */
    public void pullTalk() {
        contentManager.pullTalkFromCache(talk.getId(),
                new ContentManager.ContentCallback<Talk>() {
                    @Override
                    public void onResult(Talk result, Exception exception) {
                        if (exception != null) {
                            exception.printStackTrace();
                            return;
                        }
                        if (result != null) {
                            talk = result;
                            populateViews(talk);
                        }
                    }
                });
    }

    /**
     * Populates views with talk data
     *
     * @param talk talk object
     */
    private void populateViews(Talk talk) {
        if(getActivity() != null) {
            tvName.setText(talk.getName());
            tvDesc.setText(talk.getDesc());
            tvPublished.setText(talk.getPublishedAt());
            tvRecorded.setText(talk.getRecordedAt());
            tvUpdated.setText(talk.getUpdatedAt());
            tvViewed.setText(String.valueOf(talk.getViewedCount()));
            tvEmailed.setText(String.valueOf(talk.getEmailedCount()));
            tvCommented.setText(String.valueOf(talk.getCommentedCount()));

            initVideo(talk.getVideoHighUrl());
        }
    }

    /**
     * Initializes MediaController object to manage video
     */
    private void initMediaController() {
        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(vvTalkVideo);
//        mediaController.setMediaPlayer(vvTalkVideo);
    }

    /**
     * Initializes VideoView object to play video in
     *
     * @param url url to play from
     */
    private void initVideo(String url) {
            vvTalkVideo.setMediaController(mediaController);
            vvTalkVideo.setVideoPath(url);
            vvTalkVideo.seekTo(playPosition);
            vvTalkVideo.start();
    }


}
