package com.xtern.cultural_trail.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.adapters.IssueDetailAdapter;
import com.xtern.cultural_trail.models.Issue;
import com.xtern.cultural_trail.models.VersionModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class IssueDetailFragment extends Fragment {

    private Issue issue;
    private CollapsingToolbarLayout collapsingToolbar;
    private RecyclerView recyclerView;
    private IssueDetailAdapter issueDetailAdapter;


    public IssueDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        issue = (Issue) args.getSerializable("issue");
        Log.d("got issue", issue.name);
        View v = inflater.inflate(R.layout.fragment_issue_detail, container, false);

        final Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);


        ImageView header = (ImageView) v.findViewById(R.id.header);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

        TextView issuename = (TextView)v.findViewById(R.id.issue_name_text_view);
        TextView issuedetail = (TextView)v.findViewById(R.id.issue_detail_text_view);
        TextView location = (TextView) v.findViewById(R.id.location_text_view);
        TextView priority = (TextView)v.findViewById(R.id.priority_text_view);
        TextView reported = (TextView)v.findViewById(R.id.reported_text_view);

        issuename.setText(issue.name);
        issuedetail.setText(issue.description);
        location.setText("410 Limestone");
        priority.setText("Priority " +issue.priority);
        reported.setText("Reported by Ben Wencke" );


        return v;
    }


}
