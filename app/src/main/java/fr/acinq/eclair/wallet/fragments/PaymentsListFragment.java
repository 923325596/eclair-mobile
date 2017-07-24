package fr.acinq.eclair.wallet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.acinq.eclair.wallet.R;
import fr.acinq.eclair.wallet.adapters.PaymentListItemAdapter;
import fr.acinq.eclair.wallet.model.Payment;

public class PaymentsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

  private View mView;
  private PaymentListItemAdapter mPaymentAdapter;
  private SwipeRefreshLayout mRefreshLayout;
  private TextView mEmptyLabel;

  @Override
  public void onRefresh() {
    mPaymentAdapter.update(getPayments());
    mRefreshLayout.setRefreshing(false);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(false);
    this.mPaymentAdapter = new PaymentListItemAdapter(new ArrayList<Payment>());
  }

  @Override
  public void onResume() {
    super.onResume();
    updateList();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mView = inflater.inflate(R.layout.fragment_paymentslist, container, false);
    mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.payments_swiperefresh);
    mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.green, R.color.colorAccent);
    mRefreshLayout.setOnRefreshListener(this);
    mEmptyLabel = (TextView) mView.findViewById(R.id.payments_empty);

    RecyclerView listView = (RecyclerView) mView.findViewById(R.id.payments_list);
    listView.setHasFixedSize(true);
    listView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
    listView.setAdapter(mPaymentAdapter);

    return mView;
  }

  private List<Payment> getPayments() {
    List<Payment> list = Payment.findWithQuery(Payment.class, "SELECT * FROM Payment ORDER BY updated DESC LIMIT 100");
    if (mEmptyLabel != null) {
      if (list.isEmpty()) {
        mEmptyLabel.setVisibility(View.VISIBLE);
      } else {
        mEmptyLabel.setVisibility(View.GONE);
      }
    }
    return list;
  }

  public void updateList() {
    mPaymentAdapter.update(getPayments());
  }

}
