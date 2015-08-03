package com.xtern.cultural_trail.fragments;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.model.CardItemView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.xtern.cultural_trail.R;
import com.xtern.cultural_trail.cards.IssueListCard;
import com.xtern.cultural_trail.models.CulturalTrailRestClient;
import com.xtern.cultural_trail.models.Issue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.dexafree.materialList.view.MaterialListView;

import org.joda.time.DateTime;


public class IssuesListFragment extends Fragment {
    MaterialListView materialListView;
    private ProgressWheel progressWheel;


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
                List<Card> cards = new ArrayList();
                for (Issue issue : issues) {
                    IssueListCard issueListCard = new IssueListCard(getActivity());
                    issueListCard.setTag(issue);
                    issueListCard.setTitle(issue.name);
                    issueListCard.setDescription(issue.description);
                    issueListCard.setImageUrl(issue.picture);
                    Geocoder geocoder = new Geocoder(getActivity());
                    if (geocoder.isPresent()) {
                        try {
                            Address address = geocoder.getFromLocation(issue.location.lat, issue.location.lng, 1).get(0);
                            String addressString = address.getAddressLine(0) + " " + address.getAdminArea() + "," + address.getLocality() + " " + address.getPostalCode();
                            issueListCard.setLocation(addressString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    cards.add(issueListCard);
                    //materialListView.add(issueListCard);
                }
                materialListView.addAll(cards);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }


}
