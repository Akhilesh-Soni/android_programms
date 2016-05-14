public class CustomFragment extends Fragment implements
        UpdateListener {

    
    private CustomRecyclerViewAdapter  mCustomRecyclerViewAdapter;
	private RecyclerView mRecyclerView;
	
    public static CustomFragment newInstance() {
        return new CustomFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_sites_layout, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		mRecyclerView = (RecyclerView)getActivity.findViewById(R.id.recyclerView);
        setCustomViewAdapter();
    }

    private void setCustomViewAdapter() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCustomRecyclerViewAdapter =new CustomRecyclerViewAdapter(new ArrayList<Pojo>());
        mRecyclerView.setAdapter(mCustomRecyclerViewAdapter);
    }

    @Override
    public void updateList(List<Pojo> listOfPojos) {
        mCustomRecyclerViewAdapter.updateList(mfSites);
    }


    @Override
    public void onStart() {
        super.onStart();
        new JsonObjectRequestClient(this).getJsonObject();
        
    }

}