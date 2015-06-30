package com.xtern.cultural_trail.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dexafree.materialList.model.CardItemView;
import com.xtern.cultural_trail.R;

/**
 * Created by kyle on 6/29/15.
 */
public class IssueListCardItemView extends CardItemView<IssueListCard> {
    public IssueListCardItemView(Context context) {
        super(context);
    }

    public IssueListCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IssueListCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(final IssueListCard card) {
        super.build(card);

        //Ttitle
        TextView title = (TextView)findViewById(R.id.titleTextView);
        title.setText(card.getTitle());
        title.setTextColor(card.getTitleColor());



        // Subtitle
        TextView subtitle = (TextView) findViewById(R.id.subtitleTextView);
        subtitle.setText(card.getSubtitle());
        subtitle.setTextColor(card.getSubtitleColor());

        //Description
        TextView description = (TextView)findViewById(R.id.descriptionTextView);
        description.setText(card.getDescription());
        description.setTextColor(card.getDescriptionColor());

        //Priority
        TextView priority = (TextView)findViewById(R.id.priority_text_view);
        priority.setText("Priority: " + card.getPriority());
        priority.setTextColor(card.getPriorityColor());


        // Divider
        View divider = findViewById(R.id.cardDivider);
        divider.setBackgroundColor(card.getDividerColor());


    }

    private Drawable resize(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResize = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(getResources(), bitmapResize);
    }


}
