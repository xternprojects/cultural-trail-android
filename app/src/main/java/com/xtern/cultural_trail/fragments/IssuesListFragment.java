package com.xtern.cultural_trail.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.CardItemView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.cards.IssueListCard;
import com.xtern.cultural_trail.models.CulturalTrailRestClient;
import com.xtern.cultural_trail.models.Issue;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.dexafree.materialList.view.MaterialListView;

import org.joda.time.DateTime;


public class IssuesListFragment extends Fragment {
    MaterialListView materialListView;


    public IssuesListFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_issues_list, container, false);
        materialListView = (MaterialListView)v.findViewById(R.id.material_listview);
        materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(CardItemView cardItemView, int i) {
                Issue issue = (Issue) cardItemView.getTag();
                Log.d("clicked", issue.name);
                Bundle bundle = new Bundle();
                bundle.putSerializable("issue", issue);
                IssueDetailFragment issueDetailFragment = new IssueDetailFragment();
                issueDetailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, issueDetailFragment)
                        .addToBackStack(null)

                        .commit();

            }

            @Override
            public void onItemLongClick(CardItemView cardItemView, int i) {
            }
        });
        initIssuesList();
        FloatingActionButton floatingActionButton = (FloatingActionButton)v.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new CreateIssueFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }

    public void initIssuesList(){
        CulturalTrailRestClient.getClient().getIssues(new Callback<List<Issue>>() {
            @Override
            public void success(List<Issue> issues, Response response) {
                for(Issue issue: issues){
                    IssueListCard issueListCard = new IssueListCard(getActivity());
                    issueListCard.setTag(issue);
                    issueListCard.setTitle(issue.name);
                    issueListCard.setDescription(issue.description);
                    issueListCard.setSubtitle(DateTime.parse(issue.reportedDate).toString("dd, MMM YYYY h:mm a"));
                    issueListCard.setPriority(issue.priority);
                    materialListView.add(issueListCard);

                }


            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }


}
