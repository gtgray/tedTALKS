package tk.atna.tedtalks.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.atna.tedtalks.stuff.ContentManager;
import tk.atna.tedtalks.R;
import tk.atna.tedtalks.provider.TalksContract;


public class TalksCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;

//    private int talkIdIndex;
    private int imageIndex;
    private int nameIndex;
    private int publishedIndex;
    private int viewedIndex;
    private int commentedIndex;

    private ContentManager contentManager;


    public TalksCursorAdapter(Context context, Cursor cursor, ContentManager contentManager) {
        super(context, cursor, 0);

        this.inflater = LayoutInflater.from(context);
        this.contentManager = contentManager;

        rememberColumns(cursor);
    }

    @Override
    public View newView(Context context, final Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.fragment_feed_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ItemViewHolder holder = (ItemViewHolder) view.getTag();

        if(cursor != null && !cursor.isClosed()) {
            contentManager.getImage(cursor.getString(imageIndex), holder.ivImage);

            holder.tvName.setText(cursor.getString(nameIndex));
            holder.tvPublished.setText(cursor.getString(publishedIndex));
            holder.tvViewed.setText(cursor.getString(viewedIndex));
            holder.tvCommented.setText(cursor.getString(commentedIndex));
        }
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);

        rememberColumns(cursor);
    }

    /**
     * Remembers cursor column indexes for future reuse
     *
     * @param cursor cursor whose columns
     */
    private void rememberColumns(Cursor cursor) {
        if(cursor == null)
            return;

//        this.talkIdIndex = cursor.getColumnIndex(TalksContract.Talks._ID);
        this.imageIndex = cursor.getColumnIndex(TalksContract.Talks.TALK_IMAGE_URL);
        this.nameIndex = cursor.getColumnIndex(TalksContract.Talks.TALK_NAME);
        this.publishedIndex = cursor.getColumnIndex(TalksContract.Talks.TALK_PUBLISHED);
        this.viewedIndex = cursor.getColumnIndex(TalksContract.Talks.TALK_VIEWED);
        this.commentedIndex = cursor.getColumnIndex(TalksContract.Talks.TALK_COMMENTED);
    }

    /**
     * View holder class for list view items
     */
    class ItemViewHolder {

        @InjectView(R.id.iv_image)
        ImageView ivImage;

        @InjectView(R.id.tv_name)
        TextView tvName;

        @InjectView(R.id.tv_published)
        TextView tvPublished;

        @InjectView(R.id.tv_viewed)
        TextView tvViewed;

        @InjectView(R.id.tv_commented)
        TextView tvCommented;


        ItemViewHolder(View v) {
            ButterKnife.inject(this, v);
        }

    }
}
