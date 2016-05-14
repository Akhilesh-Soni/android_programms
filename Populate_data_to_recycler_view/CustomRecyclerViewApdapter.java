public class CustomRecyclerViewAdapter
        extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder>{

    public final List<Pojo> listsOfPojos;
    

    public CustomRecyclerViewAdapter(List<Pojo> listsOfPojos) {
        super();
        this.listsOfPojos = listsOfPojos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

       View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_layout, parent, false);
       return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 int position) {
        //only one item will be bound to the holder, since we have only one object in list
        Pojo mPojo = listOfPojos.get(position);
        holder.tvName.setText(mPojo.getName());
        holder.tvEmail.setText(mPojo.getEmail());
        holder.tvHome.setText(mPojo.getHome());
        holder.tvPhone.setText(mPojo.getPhone());
        
    }


    @Override
    public int getItemCount() {
        //it should always return 1 in your case
        return listOfPojos.size();
    }

    public void updateList(List<MFSite> viewModels) {
        listOfPojos.clear();
        listOfpojos.addAll(viewModels);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvName;
        public final TextView tvEmail;
        public final TextView tvHome;
        public final TextView tvPhone;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_email);
            tvHome = (TextView) itemView.findViewById(R.id.tv_home);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
        }
    }
}