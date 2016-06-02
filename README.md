# LoadMoreListView
只能上拉加载跟多的ListView，
-----------------------------------------------
本人水平有限，有些地方没有封装好 请大家按照个人需求进行修改，欢迎提交代码
演示：
int  mPage=0;

        oncreate(){

                 mListViewAdapter = new MaintenanceListAdapter(mContext, mMainTenanceItems);
                 mListview.setAdapter(mListViewAdapter);
                 mListview.setloadMoreListener(this);
        }
        initDate(){
                 HashMap<String, String> params = new HashMap<>();
                 params.put("method", "Test");
                 JSONObject jsonObject = new JSONObject();
                 try {
                    jsonObject.put("startIndex", mPage);
                    jsonObject.put("pageSize", ConstantUtil.PAGE_SIZE);
                 } catch (JSONException e) {
                 e.printStackTrace();
                 }
        params.put("reqBody", jsonObject.toString());
        CommonUtil.task = new AsyncNetUtil(mContext, "getMaintenanceList", false);
        CommonUtil.task.execute(params);
        CommonUtil.task.setLoadDataComplete(new AsyncNetUtil.IsDataLoadedListener() {
            @Override
            public void loadComplete(Object result) {
                MaintenanceListResponse maintenanceListResponse = (MaintenanceListResponse) result;
                if (maintenanceListResponse.errorCode == ConstantUtil.CODE_REQUEST_SUCCESS) {
                    if (maintenanceListResponse.mainTenanceItems != null && maintenanceListResponse.mainTenanceItems
                            .size() > 0) {
                        mPage++;
                        mMainTenanceItems.addAll(maintenanceListResponse.mainTenanceItems);
                        if (maintenanceListResponse.mainTenanceItems.size() < ConstantUtil.PAGE_SIZE) {
                            mListview.noMoreData();
                        }
                    } else {
                        mListview.noMoreData();
                    }
                } else {
                    mListview.setError(maintenanceListResponse.errorDesc);
                }
                mListViewAdapter.notifyDataSetChanged();
                mListview.flushFinish();
            }
        });
    }

    //回调
      @Override
    public void onLoadMore() {
            loadData("");
        
    }
