public class JsonObjectRequestClient {

    private UpdateListener mUpdateListener;

    public JsonObjectRequestClient(UpdateListener updateListener) {
        this.mUpdateListener = updateListener;
    }

    public void getJsonObject(){
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
            urlJsonObj, null, new Response.Listener<JSONObject>() {
 
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());
 
                    try {
                        // Parsing json object response
                        // response will be a json object
						Pojo pojo = new Pojo();
						List<Pojo> listOfPojo = new ArrayList<>();
                        String name = response.getString("name");
                        String email = response.getString("email");
                        JSONObject phone = response.getJSONObject("phone");
                        String home = phone.getString("home");
                        String mobile = phone.getString("mobile");
						pojo.setName(name);
						pojo.setEmail(email);
						pojo.setHome(home);
						pojo.setMobile(mobile);
						listOfPojo.add(pojo);
						mUpdateListener.updateList(listOfPojo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
 
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                
                }
            });
 
    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(jsonObjReq);
	} 
}
