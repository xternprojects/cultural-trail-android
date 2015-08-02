package com.xtern.cultural_trail.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.model.CardItemView;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
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

        TextView title = (TextView)findViewById(R.id.issue_name_text_view);
        title.setText(card.getTitle());

        TextView description = (TextView)findViewById(R.id.issue_text);
        description.setText(card.getDescription());

        ImageView issuePicture = (ImageView)findViewById(R.id.issue_image);
        Picasso
                .with(getContext())
                .load(card.getImageUrl())
                .into(issuePicture);
        //Ion.with(issuePicture).load(card.getImageUrl());

        TextView location =(TextView)findViewById(R.id.location_text_view);
        location.setText(card.getLocation());

    }

    private Drawable resize(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResize = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(getResources(), bitmapResize);
    }


}
